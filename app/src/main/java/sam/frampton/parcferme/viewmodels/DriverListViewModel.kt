package sam.frampton.parcferme.viewmodels

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import sam.frampton.parcferme.data.Driver
import sam.frampton.parcferme.data.RefreshResult
import sam.frampton.parcferme.data.repositories.DriverRepository

class DriverListViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = DriverRepository(application)

    var season: Int? = null
        private set
    private val _driverList = MediatorLiveData<List<Driver>>()
    val driverList: LiveData<List<Driver>>
        get() = _driverList
    private var seasonDriverList: LiveData<List<Driver>>? = null

    private val _networkError = MutableLiveData(false)
    val networkError: LiveData<Boolean>
        get() = _networkError
    private val _otherError = MutableLiveData(false)
    val otherError: LiveData<Boolean>
        get() = _otherError

    fun setSeason(season: Int) {
        this.season = season
        seasonDriverList?.let { _driverList.removeSource(it) }
        repository.getDrivers(season).also {
            seasonDriverList = it
            _driverList.addSource(it) { drivers -> _driverList.value = drivers }
        }
    }

    fun refreshDrivers(force: Boolean) {
        season?.let { season ->
            viewModelScope.launch {
                when (repository.refreshDrivers(season, force)) {
                    RefreshResult.NETWORK_ERROR -> _networkError.postValue(true)
                    RefreshResult.OTHER_ERROR -> _otherError.postValue(true)
                    RefreshResult.SUCCESS -> {
                    }
                    RefreshResult.CACHE -> {
                    }
                }
            }
        }
    }

    fun clearErrors() {
        _networkError.value = false
        _otherError.value = false
    }

    override fun onCleared() {
        seasonDriverList?.let { _driverList.removeSource(it) }
        seasonDriverList = null
    }
}