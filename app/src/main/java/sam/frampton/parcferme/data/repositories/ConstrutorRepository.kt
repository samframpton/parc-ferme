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
import sam.frampton.parcferme.data.Constructor
import sam.frampton.parcferme.data.RefreshResult
import sam.frampton.parcferme.data.toConstructorEntityList
import sam.frampton.parcferme.data.toConstructorList
import sam.frampton.parcferme.database.AppDatabase
import java.io.IOException

private const val LOG_TAG = "ConstructorRepository"

class ConstructorRepository(val context: Context) {

    private val constructorDao = AppDatabase.getInstance(context).constructorDao()
    private val timestampManager = TimestampManager(context)

    fun getConstructors(season: Int): LiveData<List<Constructor>> =
        Transformations.map(constructorDao.getConstructorsBySeason(season)) {
            it.constructors.toConstructorList()
        }

    suspend fun refreshConstructors(season: Int, force: Boolean = false): RefreshResult =
        withContext(Dispatchers.IO) {
            val constructorKey = context.getString(R.string.constructor_timestamp_key)
            if (!force && timestampManager.isCacheValid(constructorKey, season.toString())) {
                RefreshResult.CACHE
            } else {
                try {
                    val apiResponse = ErgastService.instance.constructors(season)
                    cacheApiConstructors(apiResponse).apply {
                        if (this == RefreshResult.SUCCESS) {
                            timestampManager.updateCacheTimestamp(constructorKey, season.toString())
                        }
                    }
                } catch (throwable: Throwable) {
                    Log.d(LOG_TAG, "refresh constructors error (season=$season)", throwable)
                    when (throwable) {
                        is IOException -> RefreshResult.NETWORK_ERROR
                        else -> RefreshResult.OTHER_ERROR
                    }
                }
            }
        }

    private fun cacheApiConstructors(response: ErgastResponse): RefreshResult =
        response.motorRacingData.constructorTable?.let { constructorTable ->
            constructorTable.season?.let {
                constructorDao.insertConstructorsBySeason(
                    it,
                    constructorTable.constructors.toConstructorEntityList()
                )
                RefreshResult.SUCCESS
            }
        } ?: RefreshResult.OTHER_ERROR
}