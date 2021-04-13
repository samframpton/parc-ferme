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

class ResultRepository(context: Context) {

    private val resultDao = AppDatabase.getInstance(context).resultDao()

    fun getRaceResults(season: Int, round: Int): LiveData<List<RaceResult>> =
        Transformations.map(resultDao.getRaceResultsBySeasonRound(season, round)) {
            it.toRaceResultList()
        }

    suspend fun refreshRaceResults(season: Int, round: Int): RefreshResult =
        withContext(Dispatchers.IO) {
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

    private fun cacheApiRaceResults(response: ErgastResponse): RefreshResult =
        response.motorRacingData.raceTable?.races?.firstOrNull()?.let { race ->
            race.results.forEach {
                resultDao.insertRaceResult(
                    it.toRaceResultEntity(race.season, race.round),
                    it.driver.toDriverEntity(),
                    it.constructor.toConstructorEntity()
                )
            }
            RefreshResult.SUCCESS
        } ?: RefreshResult.OTHER_ERROR

    fun getQualifyingResults(season: Int, round: Int): LiveData<List<QualifyingResult>> =
        Transformations.map(resultDao.getQualifyingResultsBySeasonRound(season, round)) {
            it.toQualifyingResultList()
        }

    suspend fun refreshQualifyingResults(season: Int, round: Int): RefreshResult =
        withContext(Dispatchers.IO) {
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

    private fun cacheApiQualifyingResults(response: ErgastResponse): RefreshResult =
        response.motorRacingData.raceTable?.races?.firstOrNull()?.let { race ->
            race.qualifyingResults.forEach {
                resultDao.insertQualifyingResult(
                    it.toQualifyingResultEntity(race.season, race.round),
                    it.driver.toDriverEntity(),
                    it.constructor.toConstructorEntity()
                )
            }
            RefreshResult.SUCCESS
        } ?: RefreshResult.OTHER_ERROR
}