package sam.frampton.parcferme.data.repositories

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import sam.frampton.parcferme.api.ErgastService
import sam.frampton.parcferme.api.dtos.ErgastResponse
import sam.frampton.parcferme.data.Driver
import sam.frampton.parcferme.data.RefreshResult
import sam.frampton.parcferme.data.toDriverEntityList
import sam.frampton.parcferme.data.toDriverList
import sam.frampton.parcferme.database.AppDatabase
import java.io.IOException

class DriverRepository(context: Context) {

    private val driverDao = AppDatabase.getInstance(context).driverDao()

    fun getDrivers(season: Int): LiveData<List<Driver>> =
        Transformations.map(driverDao.getDriversBySeason(season)) {
            it.drivers.toDriverList()
        }

    suspend fun refreshDrivers(season: Int): RefreshResult =
        withContext(Dispatchers.IO) {
            try {
                val apiResponse = ErgastService.instance.drivers(season)
                cacheApiDrivers(apiResponse)
            } catch (throwable: Throwable) {
                when (throwable) {
                    is IOException -> RefreshResult.NETWORK_ERROR
                    else -> RefreshResult.OTHER_ERROR
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