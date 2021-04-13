package sam.frampton.parcferme.data.repositories

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import sam.frampton.parcferme.api.ErgastService
import sam.frampton.parcferme.api.dtos.ErgastResponse
import sam.frampton.parcferme.data.RefreshResult
import sam.frampton.parcferme.data.toSeasonEntityList
import sam.frampton.parcferme.data.toSeasonList
import sam.frampton.parcferme.database.AppDatabase
import java.io.IOException

class SeasonRepository(context: Context) {

    private val seasonDao = AppDatabase.getInstance(context).seasonDao()

    fun getSeasons(): LiveData<List<Int>> {
        return Transformations.map(seasonDao.getSeasons()) { it.toSeasonList() }
    }

    suspend fun refreshSeasons(): RefreshResult {
        return withContext(Dispatchers.IO) {
            try {
                val apiResponse = ErgastService.instance.seasons()
                cacheApiSeasons(apiResponse)
            } catch (throwable: Throwable) {
                when (throwable) {
                    is IOException -> RefreshResult.NETWORK_ERROR
                    else -> RefreshResult.OTHER_ERROR
                }
            }
        }
    }

    private fun cacheApiSeasons(response: ErgastResponse): RefreshResult {
        val seasons = response.motorRacingData.seasonTable?.seasons?.toSeasonEntityList()
            ?: return RefreshResult.OTHER_ERROR
        seasonDao.insertSeasons(seasons)
        return RefreshResult.SUCCESS
    }
}