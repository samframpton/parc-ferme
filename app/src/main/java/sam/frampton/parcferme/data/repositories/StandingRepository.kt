package sam.frampton.parcferme.data.repositories

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import sam.frampton.parcferme.api.ErgastService
import sam.frampton.parcferme.api.dtos.ErgastResponse
import sam.frampton.parcferme.data.*
import sam.frampton.parcferme.database.AppDatabase
import java.io.IOException

class StandingRepository(context: Context) {

    private val standingDao = AppDatabase.getInstance(context).standingDao()

    fun getDriverStandingsBySeason(season: Int): LiveData<List<DriverStanding>> =
        Transformations.map(standingDao.getDriverStandingsBySeason(season)) {
            it.toDriverStandingList()
        }

    suspend fun refreshDriverStandingsBySeason(season: Int): RefreshResult =
        withContext(Dispatchers.IO) {
            try {
                val apiResponse = ErgastService.instance.driverStandings(season)
                cacheApiDriverStandings(apiResponse)
            } catch (throwable: Throwable) {
                when (throwable) {
                    is IOException -> RefreshResult.NETWORK_ERROR
                    else -> RefreshResult.OTHER_ERROR
                }
            }
        }

    fun getDriverStandingsByDriver(driverId: String): LiveData<List<DriverStanding>> =
        Transformations.map(standingDao.getDriverStandingsByDriver(driverId)) {
            it.toDriverStandingList()
        }

    suspend fun refreshDriverStandingsByDriver(driverId: String): RefreshResult =
        withContext(Dispatchers.IO) {
            try {
                val apiResponse = ErgastService.instance.driverStandings(driverId)
                cacheApiDriverStandings(apiResponse)
            } catch (throwable: Throwable) {
                when (throwable) {
                    is IOException -> RefreshResult.NETWORK_ERROR
                    else -> RefreshResult.OTHER_ERROR
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
        Transformations.map(standingDao.getConstructorStandingsBySeason(season)) {
            it.toConstructorStandingList()
        }

    suspend fun refreshConstructorStandingsBySeason(season: Int): RefreshResult =
        withContext(Dispatchers.IO) {
            try {
                val apiResponse = ErgastService.instance.constructorStandings(season)
                cacheApiConstructorStandings(apiResponse)
            } catch (throwable: Throwable) {
                when (throwable) {
                    is IOException -> RefreshResult.NETWORK_ERROR
                    else -> RefreshResult.OTHER_ERROR
                }
            }
        }

    fun getConstructorStandingsByConstructor(constructorId: String): LiveData<List<ConstructorStanding>> =
        Transformations.map(standingDao.getConstructorStandingsByConstructor(constructorId)) {
            it.toConstructorStandingList()
        }

    suspend fun refreshConstructorStandingsByConstructor(constructorId: String): RefreshResult =
        withContext(Dispatchers.IO) {
            try {
                val apiResponse = ErgastService.instance.constructorStandings(constructorId)
                cacheApiConstructorStandings(apiResponse)
            } catch (throwable: Throwable) {
                when (throwable) {
                    is IOException -> RefreshResult.NETWORK_ERROR
                    else -> RefreshResult.OTHER_ERROR
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