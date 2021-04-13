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

class RaceRepository(val context: Context) {

    private val raceDao = AppDatabase.getInstance(context).raceDao()

    fun getRaces(season: Int): LiveData<List<Race>> =
        Transformations.map(raceDao.getRacesBySeason(season)) { it.toRaceList() }

    suspend fun refreshRaces(season: Int): RefreshResult =
        withContext(Dispatchers.IO) {
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

    private fun cacheApiRaces(response: ErgastResponse): RefreshResult =
        response.motorRacingData.raceTable?.races?.let { races ->
            races.forEach { raceDao.insertRace(it.toRaceEntity(), it.circuit.toCircuitEntity()) }
            RefreshResult.SUCCESS
        } ?: RefreshResult.OTHER_ERROR
}