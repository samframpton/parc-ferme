package sam.frampton.parcferme.data.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import sam.frampton.parcferme.api.ErgastService
import sam.frampton.parcferme.api.dtos.ErgastResponse
import sam.frampton.parcferme.data.Driver
import sam.frampton.parcferme.data.RefreshResult
import sam.frampton.parcferme.data.toDriverList
import java.io.IOException

class DriverRepository() {

    private val driverList = MutableLiveData<List<Driver>>(emptyList())

    fun getDrivers(season: Int): LiveData<List<Driver>> {
        return driverList
    }

    suspend fun refreshDrivers(season: Int): RefreshResult {
        return withContext(Dispatchers.IO) {
            try {
                val apiResponse = ErgastService.instance.drivers(season)
                cacheApiDrivers(apiResponse)
            } catch (throwable: Throwable) {
                when (throwable) {
                    is IOException -> RefreshResult.NETWORK_ERROR
                    else -> RefreshResult.OTHER_ERROR
                }
            }
        }
    }

    private fun cacheApiDrivers(response: ErgastResponse): RefreshResult {
        val drivers = response.motorRacingData.driverTable?.drivers?.toDriverList()
            ?: return RefreshResult.OTHER_ERROR
        driverList.postValue(drivers)
        return RefreshResult.SUCCESS
    }
}