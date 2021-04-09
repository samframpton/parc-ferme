package sam.frampton.parcferme.data.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import sam.frampton.parcferme.api.ErgastService
import sam.frampton.parcferme.api.dtos.ErgastResponse
import sam.frampton.parcferme.data.*
import java.io.IOException

class StandingRepository() {

    private val driverStandingList = MutableLiveData<List<DriverStanding>>(emptyList())
    private val constructorStandingList = MutableLiveData<List<ConstructorStanding>>(emptyList())

    fun getDriverStandingsBySeason(season: Int): LiveData<List<DriverStanding>> {
        return driverStandingList
    }

    suspend fun refreshDriverStandingsBySeason(season: Int): RefreshResult {
        return withContext(Dispatchers.IO) {
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
    }

    fun getDriverStandingsByDriver(driverId: String): LiveData<List<DriverStanding>> {
        return driverStandingList
    }

    suspend fun refreshDriverStandingsByDriver(driverId: String): RefreshResult {
        return withContext(Dispatchers.IO) {
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
    }

    private fun cacheApiDriverStandings(response: ErgastResponse): RefreshResult {
        val standingsLists = response.motorRacingData.standingsTable?.standingsLists
            ?: return RefreshResult.OTHER_ERROR
        var result = RefreshResult.SUCCESS
        val driverStandings = ArrayList<DriverStanding>()
        standingsLists.forEach { standingsList ->
            if (standingsList.driverStandings == null) {
                result = RefreshResult.OTHER_ERROR
            } else {
                standingsList.driverStandings.forEach { driverStanding ->
                    driverStandings.add(driverStanding.toDriverStanding(standingsList.season))
                }
            }
        }
        driverStandingList.postValue(driverStandings)
        return result
    }

    fun getConstructorStandingsBySeason(season: Int): LiveData<List<ConstructorStanding>> {
        return constructorStandingList
    }

    suspend fun refreshConstructorStandingsBySeason(season: Int): RefreshResult {
        return withContext(Dispatchers.IO) {
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
    }

    fun getConstructorStandingsByConstructor(constructorId: String)
            : LiveData<List<ConstructorStanding>> {
        return constructorStandingList
    }

    suspend fun refreshConstructorStandingsByConstructor(constructorId: String): RefreshResult {
        return withContext(Dispatchers.IO) {
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
    }

    private fun cacheApiConstructorStandings(response: ErgastResponse): RefreshResult {
        val standingsLists = response.motorRacingData.standingsTable?.standingsLists
            ?: return RefreshResult.OTHER_ERROR
        var result = RefreshResult.SUCCESS
        val constructorStandings = ArrayList<ConstructorStanding>()
        standingsLists.forEach { standingsList ->
            if (standingsList.constructorStandings == null) {
                result = RefreshResult.OTHER_ERROR
            } else {
                standingsList.constructorStandings.forEach { constructorStanding ->
                    constructorStandings.add(
                        constructorStanding.toConstructorStanding(standingsList.season)
                    )
                }
            }
        }
        constructorStandingList.postValue(constructorStandings)
        return result
    }
}