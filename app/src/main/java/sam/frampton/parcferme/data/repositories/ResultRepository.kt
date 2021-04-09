package sam.frampton.parcferme.data.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import sam.frampton.parcferme.api.ErgastService
import sam.frampton.parcferme.api.dtos.ErgastResponse
import sam.frampton.parcferme.data.*
import java.io.IOException

class ResultRepository() {

    private val raceResultList = MutableLiveData<List<RaceResult>>(emptyList())
    private val qualifyingResultList = MutableLiveData<List<QualifyingResult>>(emptyList())

    fun getRaceResults(season: Int, round: Int): LiveData<List<RaceResult>> {
        return raceResultList
    }

    suspend fun refreshRaceResults(season: Int, round: Int): RefreshResult {
        return withContext(Dispatchers.IO) {
            try {
                val apiResponse = ErgastService.instance.raceResults(season, round)
                cacheApiRaceResults(apiResponse)
            } catch (throwable: Throwable) {
                when (throwable) {
                    is IOException -> RefreshResult.NETWORK_ERROR
                    else -> RefreshResult.OTHER_ERROR
                }
            }
        }
    }

    private fun cacheApiRaceResults(response: ErgastResponse): RefreshResult {
        val raceResults =
            response.motorRacingData.raceTable?.races?.firstOrNull()?.results?.toRaceResultList()
                ?: return RefreshResult.OTHER_ERROR
        raceResultList.postValue(raceResults)
        return RefreshResult.SUCCESS
    }

    fun getQualifyingResults(season: Int, round: Int): LiveData<List<QualifyingResult>> {
        return qualifyingResultList
    }

    suspend fun refreshQualifyingResults(season: Int, round: Int): RefreshResult {
        return withContext(Dispatchers.IO) {
            try {
                val apiResponse = ErgastService.instance.qualifyingResults(season, round)
                cacheApiQualifyingResults(apiResponse)
            } catch (throwable: Throwable) {
                when (throwable) {
                    is IOException -> RefreshResult.NETWORK_ERROR
                    else -> RefreshResult.OTHER_ERROR
                }
            }
        }
    }

    private fun cacheApiQualifyingResults(response: ErgastResponse): RefreshResult {
        val qualifyingResults = response.motorRacingData.raceTable?.races?.firstOrNull()
            ?.qualifyingResults?.toQualifyingResultList() ?: return RefreshResult.OTHER_ERROR
        qualifyingResultList.postValue(qualifyingResults)
        return RefreshResult.SUCCESS
    }
}