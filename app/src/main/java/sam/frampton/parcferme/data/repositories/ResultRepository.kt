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
import sam.frampton.parcferme.database.entities.ConstructorEntity
import sam.frampton.parcferme.database.entities.DriverEntity
import sam.frampton.parcferme.database.entities.QualifyingResultEntity
import sam.frampton.parcferme.database.entities.RaceResultEntity
import java.io.IOException

private const val LOG_TAG = "ResultRepository"

class ResultRepository(val context: Context) {

    private val resultDao = AppDatabase.getInstance(context).resultDao()
    private val timestampManager = TimestampManager(context)

    fun getRaceResults(season: Int, round: Int): LiveData<List<RaceResult>> =
        Transformations.map(resultDao.getRaceResultsBySeasonRound(season, round)) { results ->
            results.toRaceResultList().sortedBy { it.position }
        }

    suspend fun refreshRaceResults(season: Int, round: Int, force: Boolean): RefreshResult =
        withContext(Dispatchers.IO) {
            val raceKey = context.getString(R.string.race_result_timestamp_key)
            if (!force && timestampManager.isCacheValid(raceKey, season.toString())) {
                RefreshResult.CACHE
            } else {
                try {
                    val apiResponse = ErgastService.instance.raceResults(season, round)
                    cacheApiRaceResults(apiResponse).apply {
                        if (this == RefreshResult.SUCCESS) {
                            timestampManager.updateCacheTimestamp(
                                raceKey,
                                "$season$round"
                            )
                        }
                    }
                } catch (throwable: Throwable) {
                    Log.d(
                        LOG_TAG,
                        "refresh race results error (season=$season, round=$round)",
                        throwable
                    )
                    when (throwable) {
                        is IOException -> RefreshResult.NETWORK_ERROR
                        else -> RefreshResult.OTHER_ERROR
                    }
                }
            }
        }

    private fun cacheApiRaceResults(response: ErgastResponse): RefreshResult =
        response.motorRacingData.raceTable?.races?.firstOrNull()?.let { race ->
            val raceResultList = ArrayList<RaceResultEntity>()
            val driverList = ArrayList<DriverEntity>()
            val constructorList = ArrayList<ConstructorEntity>()
            race.results.forEach { result ->
                raceResultList.add(result.toRaceResultEntity(race.season, race.round))
                driverList.add(result.driver.toDriverEntity())
                constructorList.add(result.constructor.toConstructorEntity())
            }
            resultDao.insertRaceResults(raceResultList, driverList, constructorList)
            RefreshResult.SUCCESS
        } ?: RefreshResult.NO_RACE_DATA

    fun getQualifyingResults(season: Int, round: Int): LiveData<List<QualifyingResult>> =
        Transformations.map(resultDao.getQualifyingResultsBySeasonRound(season, round)) { results ->
            results.toQualifyingResultList().sortedBy { it.position }
        }

    suspend fun refreshQualifyingResults(season: Int, round: Int, force: Boolean): RefreshResult =
        withContext(Dispatchers.IO) {
            val qualifyingKey = context.getString(R.string.qualifying_result_timestamp_key)
            if (!force &&
                timestampManager.isCacheValid(qualifyingKey, season.toString())
            ) {
                RefreshResult.CACHE
            } else {
                try {
                    val apiResponse = ErgastService.instance.qualifyingResults(season, round)
                    cacheApiQualifyingResults(apiResponse).apply {
                        if (this == RefreshResult.SUCCESS) {
                            timestampManager.updateCacheTimestamp(
                                qualifyingKey,
                                "$season$round"
                            )
                        }
                    }
                } catch (throwable: Throwable) {
                    Log.d(
                        LOG_TAG,
                        "refresh qualifying results error (season=$season, round=$round)",
                        throwable
                    )
                    when (throwable) {
                        is IOException -> RefreshResult.NETWORK_ERROR
                        else -> RefreshResult.OTHER_ERROR
                    }
                }
            }
        }

    private fun cacheApiQualifyingResults(response: ErgastResponse): RefreshResult =
        response.motorRacingData.raceTable?.races?.firstOrNull()?.let { race ->
            val qualifyingResultList = ArrayList<QualifyingResultEntity>()
            val driverList = ArrayList<DriverEntity>()
            val constructorList = ArrayList<ConstructorEntity>()
            race.qualifyingResults.forEach { result ->
                qualifyingResultList.add(result.toQualifyingResultEntity(race.season, race.round))
                driverList.add(result.driver.toDriverEntity())
                constructorList.add(result.constructor.toConstructorEntity())
            }
            resultDao.insertQualifyingResults(qualifyingResultList, driverList, constructorList)
            RefreshResult.SUCCESS
        } ?: RefreshResult.NO_RACE_DATA
}