package sam.frampton.parcferme.data.repositories

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import sam.frampton.parcferme.api.ErgastService
import sam.frampton.parcferme.api.dtos.ErgastResponse
import sam.frampton.parcferme.data.Race
import sam.frampton.parcferme.data.RefreshResult
import sam.frampton.parcferme.data.toRaceList
import java.io.IOException

class RaceRepository(val context: Context) {

    private val raceList = MutableLiveData<List<Race>>(emptyList())

    fun getRaces(season: Int): LiveData<List<Race>> {
        return raceList
    }

    suspend fun refreshRaces(season: Int): RefreshResult {
        return withContext(Dispatchers.IO) {
            try {
                val apiResponse = ErgastService.instance.races(season)
                cacheApiRaces(apiResponse)
            } catch (throwable: Throwable) {
                when (throwable) {
                    is IOException -> RefreshResult.NETWORK_ERROR
                    else -> RefreshResult.OTHER_ERROR
                }
            }
        }
    }

    private fun cacheApiRaces(response: ErgastResponse): RefreshResult {
        val races = response.motorRacingData.raceTable?.races?.toRaceList()
            ?: return RefreshResult.OTHER_ERROR
        raceList.postValue(races)
        return RefreshResult.SUCCESS
    }
}