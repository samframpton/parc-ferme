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

    private val _refreshResult: MutableLiveData<RefreshResult> = MutableLiveData()
    val refreshResult: LiveData<RefreshResult>
        get() = _refreshResult
    private val _isRefreshing: MutableLiveData<Boolean> = MutableLiveData()
    val isRefreshing: LiveData<Boolean>
        get() = _isRefreshing

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
            _isRefreshing.value = true
            viewModelScope.launch {
                _refreshResult.value = repository.refreshDrivers(season, force)
                _isRefreshing.value = false
            }
        }
    }

    fun clearRefreshResult() {
        _refreshResult.value = null
    }

    override fun onCleared() {
        seasonDriverList?.let { _driverList.removeSource(it) }
        seasonDriverList = null
    }
}