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
import sam.frampton.parcferme.data.*
import sam.frampton.parcferme.database.AppDatabase
import java.io.IOException

private const val LOG_TAG = "StandingRepository"

class StandingRepository(val context: Context) {

    private val standingDao = AppDatabase.getInstance(context).standingDao()
    private val timestampManager = TimestampManager(context)

    fun getDriverStandingsBySeason(season: Int): LiveData<List<DriverStanding>> =
        Transformations.map(standingDao.getDriverStandingsBySeason(season)) { standings ->
            standings.toDriverStandingList().sortedBy { it.position }
        }

    suspend fun refreshDriverStandingsBySeason(season: Int, force: Boolean = false): RefreshResult =
        withContext(Dispatchers.IO) {
            val driverKey = context.getString(R.string.driver_standing_timestamp_key)
            if (!force && timestampManager.isCacheValid(driverKey, season.toString())) {
                RefreshResult.CACHE
            } else {
                try {
                    val apiResponse = ErgastService.instance.driverStandings(season)
                    cacheApiDriverStandings(apiResponse).apply {
                        if (this == RefreshResult.SUCCESS) {
                            timestampManager.updateCacheTimestamp(driverKey, season.toString())
                        }
                    }
                } catch (throwable: Throwable) {
                    Log.d(
                        LOG_TAG,
                        "refresh driver standings error (season=$season)",
                        throwable
                    )
                    when (throwable) {
                        is IOException -> RefreshResult.NETWORK_ERROR
                        else -> RefreshResult.OTHER_ERROR
                    }
                }
            }
        }

    fun getDriverStandingsByDriver(driverId: String): LiveData<List<DriverStanding>> =
        Transformations.map(standingDao.getDriverStandingsByDriver(driverId)) { standings ->
            standings.toDriverStandingList().sortedByDescending { it.season }
        }

    suspend fun refreshDriverStandingsByDriver(
        driverId: String,
        force: Boolean = false
    ): RefreshResult =
        withContext(Dispatchers.IO) {
            val driverKey = context.getString(R.string.driver_standing_timestamp_key)
            if (!force && timestampManager.isCacheValid(driverKey, driverId)) {
                RefreshResult.CACHE
            } else {
                try {
                    val apiResponse = ErgastService.instance.driverStandings(driverId)
                    cacheApiDriverStandings(apiResponse).apply {
                        if (this == RefreshResult.SUCCESS) {
                            timestampManager.updateCacheTimestamp(driverKey, driverId)
                        }
                    }
                } catch (throwable: Throwable) {
                    Log.d(
                        LOG_TAG,
                        "refresh driver standings error (driverId=$driverId)",
                        throwable
                    )
                    when (throwable) {
                        is IOException -> RefreshResult.NETWORK_ERROR
                        else -> RefreshResult.OTHER_ERROR
                    }
                }
            }
        }

    private fun cacheApiDriverStandings(response: ErgastResponse): RefreshResult =
        response.motorRacingData.standingsTable?.standingsLists?.let { standingsLists ->
            standingsLists.forEach { standingsList ->
                standingsList.driverStandings?.forEach { driverStanding ->
                    standingDao.insertDriverStanding(
                        driverStanding.toDriverStandingEntity(standingsList.season),
                        driverStanding.driver.toDriverEntity(),
                        driverStanding.constructors.toConstructorEntityList()
                    )
                }
            }
            return RefreshResult.SUCCESS
        } ?: RefreshResult.OTHER_ERROR

    fun getConstructorStandingsBySeason(season: Int): LiveData<List<ConstructorStanding>> =
        Transformations.map(standingDao.getConstructorStandingsBySeason(season)) { standings ->
            standings.toConstructorStandingList().sortedBy { it.position }
        }

    suspend fun refreshConstructorStandingsBySeason(
        season: Int,
        force: Boolean = false
    ): RefreshResult =
        withContext(Dispatchers.IO) {
            val constructorKey = context.getString(R.string.constructor_standing_timestamp_key)
            if (!force && timestampManager.isCacheValid(constructorKey, season.toString())) {
                RefreshResult.CACHE
            } else {
                try {
                    val apiResponse = ErgastService.instance.constructorStandings(season)
                    cacheApiConstructorStandings(apiResponse).apply {
                        if (this == RefreshResult.SUCCESS) {
                            timestampManager.updateCacheTimestamp(constructorKey, season.toString())
                        }
                    }
                } catch (throwable: Throwable) {
                    Log.d(
                        LOG_TAG,
                        "refresh constructor standings error (season=$season)",
                        throwable
                    )
                    when (throwable) {
                        is IOException -> RefreshResult.NETWORK_ERROR
                        else -> RefreshResult.OTHER_ERROR
                    }
                }
            }
        }

    fun getConstructorStandingsByConstructor(constructorId: String):
            LiveData<List<ConstructorStanding>> =
        Transformations.map(
            standingDao.getConstructorStandingsByConstructor(constructorId)
        ) { standings ->
            standings.toConstructorStandingList().sortedByDescending { it.season }
        }

    suspend fun refreshConstructorStandingsByConstructor(
        constructorId: String,
        force: Boolean = false
    ): RefreshResult =
        withContext(Dispatchers.IO) {
            val constructorKey = context.getString(R.string.constructor_standing_timestamp_key)
            if (!force && timestampManager.isCacheValid(constructorKey, constructorId)) {
                RefreshResult.CACHE
            } else {
                try {
                    val apiResponse = ErgastService.instance.constructorStandings(constructorId)
                    cacheApiConstructorStandings(apiResponse).apply {
                        if (this == RefreshResult.SUCCESS) {
                            timestampManager.updateCacheTimestamp(constructorKey, constructorId)
                        }
                    }
                } catch (throwable: Throwable) {
                    Log.d(
                        LOG_TAG,
                        "refresh constructor standings error (constructorId=$constructorId)",
                        throwable
                    )
                    when (throwable) {
                        is IOException -> RefreshResult.NETWORK_ERROR
                        else -> RefreshResult.OTHER_ERROR
                    }
                }
            }
        }

    private fun cacheApiConstructorStandings(response: ErgastResponse): RefreshResult =
        response.motorRacingData.standingsTable?.standingsLists?.let { standingsLists ->
            standingsLists.forEach { standingsList ->
                standingsList.constructorStandings?.forEach { constructorStanding ->
                    standingDao.insertConstructorStanding(
                        constructorStanding.toConstructorStandingEntity(standingsList.season),
                        constructorStanding.constructor.toConstructorEntity()
                    )
                }
            }
            return RefreshResult.SUCCESS
        } ?: RefreshResult.OTHER_ERROR
}