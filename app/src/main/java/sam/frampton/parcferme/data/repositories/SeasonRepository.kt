package sam.frampton.parcferme.data.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import sam.frampton.parcferme.api.ErgastService
import sam.frampton.parcferme.api.dtos.ErgastResponse
import sam.frampton.parcferme.data.RefreshResult
import sam.frampton.parcferme.data.toSeasonList
import java.io.IOException

class SeasonRepository() {

    private val seasonList = MutableLiveData<List<Int>>(emptyList())

    fun getSeasons(): LiveData<List<Int>> {
        return seasonList
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
        val seasons = response.motorRacingData.seasonTable?.seasons?.toSeasonList()
            ?: return RefreshResult.OTHER_ERROR
        seasonList.postValue(seasons)
        return RefreshResult.SUCCESS
    }
}