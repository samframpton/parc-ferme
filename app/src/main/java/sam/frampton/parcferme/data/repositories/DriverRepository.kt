package sam.frampton.parcferme.data.repositories

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import sam.frampton.parcferme.R
import sam.frampton.parcferme.api.ErgastService
import sam.frampton.parcferme.api.dtos.ErgastResponse
import sam.frampton.parcferme.data.Driver
import sam.frampton.parcferme.data.RefreshResult
import sam.frampton.parcferme.data.toDriverEntityList
import sam.frampton.parcferme.data.toDriverList
import sam.frampton.parcferme.database.AppDatabase
import java.io.IOException

private const val LOG_TAG = "DriverRepository"

class DriverRepository(val context: Context) {

    private val driverDao = AppDatabase.getInstance(context).driverDao()
    private val timestampManager = TimestampManager(context)

    fun getDrivers(season: Int): LiveData<List<Driver>> =
        Transformations.map(driverDao.getDriversBySeason(season)) { it.drivers.toDriverList() }

    suspend fun refreshDrivers(season: Int, force: Boolean = false): RefreshResult =
        withContext(Dispatchers.IO) {
            val driverKey = context.getString(R.string.driver_timestamp_key)
            if (!force && timestampManager.isCacheValid(driverKey, season.toString())) {
                RefreshResult.CACHE
            } else {
                try {
                    val apiResponse = ErgastService.instance.drivers(season)
                    cacheApiDrivers(apiResponse).apply {
                        if (this == RefreshResult.SUCCESS) {
                            timestampManager.updateCacheTimestamp(driverKey, season.toString())
                        }
                    }
                } catch (throwable: Throwable) {
                    Log.d(LOG_TAG, "refresh drivers error (season=$season)", throwable)
                    when (throwable) {
                        is IOException -> RefreshResult.NETWORK_ERROR
                        else -> RefreshResult.OTHER_ERROR
                    }
                }
            }
        }

    private fun cacheApiDrivers(response: ErgastResponse): RefreshResult =
        response.motorRacingData.driverTable?.let { driverTable ->
            driverTable.season?.let {
                driverDao.insertDriversBySeason(it, driverTable.drivers.toDriverEntityList())
                RefreshResult.SUCCESS
            }
        } ?: RefreshResult.OTHER_ERROR
}