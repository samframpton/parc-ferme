package sam.frampton.parcferme.data.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import sam.frampton.parcferme.api.ErgastService
import sam.frampton.parcferme.api.dtos.ErgastResponse
import sam.frampton.parcferme.data.Constructor
import sam.frampton.parcferme.data.RefreshResult
import sam.frampton.parcferme.data.toConstructorList
import java.io.IOException

class ConstructorRepository() {

    private val constructorList = MutableLiveData<List<Constructor>>(emptyList())

    fun getConstructors(season: Int): LiveData<List<Constructor>> {
        return constructorList
    }

    suspend fun refreshConstructors(season: Int): RefreshResult {
        return withContext(Dispatchers.IO) {
            try {
                val apiResponse = ErgastService.instance.constructors(season)
                cacheApiResponse(apiResponse)
            } catch (throwable: Throwable) {
                when (throwable) {
                    is IOException -> RefreshResult.NETWORK_ERROR
                    else -> RefreshResult.OTHER_ERROR
                }
            }
        }
    }

    private fun cacheApiResponse(response: ErgastResponse): RefreshResult {
        val constructors =
            response.motorRacingData.constructorTable?.constructors?.toConstructorList()
                ?: return RefreshResult.OTHER_ERROR
        constructorList.postValue(constructors)
        return RefreshResult.SUCCESS
    }
}