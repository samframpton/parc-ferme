package sam.frampton.parcferme.data.repositories

import android.content.Context
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

class RaceRepository(val context: Context) {

    private val raceDao = AppDatabase.getInstance(context).raceDao()
    private val timestampManager = TimestampManager(context)

    fun getRaces(season: Int): LiveData<List<Race>> =
        Transformations.map(raceDao.getRacesBySeason(season)) { it.toRaceList() }

    suspend fun refreshRaces(season: Int, force: Boolean = false): RefreshResult =
        withContext(Dispatchers.IO) {
            val raceKey = context.getString(R.string.race_timestamp_key)
            if (!force && timestampManager.isCacheValid(raceKey, season.toString())) {
                RefreshResult.CACHE
            } else {
                try {
                    val apiResponse = ErgastService.instance.races(season)
                    cacheApiRaces(apiResponse).apply {
                        if (this == RefreshResult.SUCCESS) {
                            timestampManager.updateCacheTimestamp(raceKey, season.toString())
                        }
                    }
                } catch (throwable: Throwable) {
                    when (throwable) {
                        is IOException -> RefreshResult.NETWORK_ERROR
                        else -> RefreshResult.OTHER_ERROR
                    }
                }
            }
        }

    private fun cacheApiRaces(response: ErgastResponse): RefreshResult =
        response.motorRacingData.raceTable?.races?.let { races ->
            races.forEach { raceDao.insertRace(it.toRaceEntity(), it.circuit.toCircuitEntity()) }
            RefreshResult.SUCCESS
        } ?: RefreshResult.OTHER_ERROR
}