package sam.frampton.parcferme.data.repositories

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import sam.frampton.parcferme.R
import sam.frampton.parcferme.api.ErgastService
import sam.frampton.parcferme.api.dtos.ErgastResponse
import sam.frampton.parcferme.data.RefreshResult
import sam.frampton.parcferme.data.toSeasonEntityList
import sam.frampton.parcferme.data.toSeasonList
import sam.frampton.parcferme.database.AppDatabase
import java.io.IOException

class SeasonRepository(val context: Context) {

    private val seasonDao = AppDatabase.getInstance(context).seasonDao()
    private val timestampManager = TimestampManager(context)

    fun getSeasons(): LiveData<List<Int>> =
        Transformations.map(seasonDao.getSeasons()) { it.toSeasonList() }

    suspend fun refreshSeasons(force: Boolean = false): RefreshResult =
        withContext(Dispatchers.IO) {
            val seasonKey = context.getString(R.string.season_timestamp_key)
            if (!force && timestampManager.isCacheValid(seasonKey)) {
                RefreshResult.CACHE
            } else {
                try {
                    val apiResponse = ErgastService.instance.seasons()
                    cacheApiSeasons(apiResponse).apply {
                        if (this == RefreshResult.SUCCESS) {
                            timestampManager.updateCacheTimestamp(seasonKey)
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

    private fun cacheApiSeasons(response: ErgastResponse): RefreshResult {
        val seasons = response.motorRacingData.seasonTable?.seasons?.toSeasonEntityList()
            ?: return RefreshResult.OTHER_ERROR
        seasonDao.insertSeasons(seasons)
        return RefreshResult.SUCCESS
    }
}