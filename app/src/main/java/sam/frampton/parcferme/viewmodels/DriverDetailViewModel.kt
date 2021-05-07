package sam.frampton.parcferme.viewmodels

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import sam.frampton.parcferme.data.Driver
import sam.frampton.parcferme.data.DriverStanding
import sam.frampton.parcferme.data.RefreshResult
import sam.frampton.parcferme.data.repositories.StandingRepository

class DriverDetailViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = StandingRepository(application)

    var driver: Driver? = null
        private set
    private val _standingList = MediatorLiveData<List<DriverStanding>>()
    val standingList: LiveData<List<DriverStanding>>
        get() = _standingList
    private var driverStandingList: LiveData<List<DriverStanding>>? = null

    private val _refreshResult: MutableLiveData<RefreshResult> = MutableLiveData()
    val refreshResult: LiveData<RefreshResult>
        get() = _refreshResult
    private val _isRefreshing: MutableLiveData<Boolean> = MutableLiveData()
    val isRefreshing: LiveData<Boolean>
        get() = _isRefreshing

    fun setDriver(driver: Driver) {
        this.driver = driver
        driverStandingList?.let { _standingList.removeSource(it) }
        repository.getDriverStandingsByDriver(driver.driverId).also {
            driverStandingList = it
            _standingList.addSource(it) { standings -> _standingList.value = standings }
        }
    }

    fun refreshDriverStandings(force: Boolean) {
        driver?.let { driver ->
            _isRefreshing.value = true
            viewModelScope.launch {
                _refreshResult.value =
                    repository.refreshDriverStandingsByDriver(driver.driverId, force)
                _isRefreshing.value = false
            }
        }
    }

    fun clearRefreshResult() {
        _refreshResult.value = null
    }

    override fun onCleared() {
        driverStandingList?.let { _standingList.removeSource(it) }
        driverStandingList = null
    }
}