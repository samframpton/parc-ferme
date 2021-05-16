package sam.frampton.parcferme.viewmodels

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import sam.frampton.parcferme.data.ConstructorStanding
import sam.frampton.parcferme.data.DriverStanding
import sam.frampton.parcferme.data.RefreshResult
import sam.frampton.parcferme.data.StandingType
import sam.frampton.parcferme.data.repositories.StandingRepository

class StandingListViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = StandingRepository(application)

    var season: Int? = null
        private set
    var standingType: StandingType = StandingType.DEFAULT

    private val _driverStandingList = MediatorLiveData<List<DriverStanding>>()
    val driverStandingList: LiveData<List<DriverStanding>>
        get() = _driverStandingList
    private var seasonDriverStandingList: LiveData<List<DriverStanding>>? = null

    private val _driverStandingRefreshResult: MutableLiveData<RefreshResult> = MutableLiveData()
    val driverStandingRefreshResult: LiveData<RefreshResult>
        get() = _driverStandingRefreshResult
    private val _driverStandingIsRefreshing: MutableLiveData<Boolean> = MutableLiveData()
    val driverStandingIsRefreshing: LiveData<Boolean>
        get() = _driverStandingIsRefreshing

    private val _constructorStandingList = MediatorLiveData<List<ConstructorStanding>>()
    val constructorStandingList: LiveData<List<ConstructorStanding>>
        get() = _constructorStandingList
    private var seasonConstructorStandingList: LiveData<List<ConstructorStanding>>? = null

    private val _constructorStandingRefreshResult: MutableLiveData<RefreshResult> =
        MutableLiveData()
    val constructorStandingRefreshResult: LiveData<RefreshResult>
        get() = _constructorStandingRefreshResult
    private val _constructorStandingIsRefreshing: MutableLiveData<Boolean> = MutableLiveData()
    val constructorStandingIsRefreshing: LiveData<Boolean>
        get() = _constructorStandingIsRefreshing

    fun setSeason(season: Int) {
        this.season = season
        seasonDriverStandingList?.let { _driverStandingList.removeSource(it) }
        repository.getDriverStandingsBySeason(season).also {
            seasonDriverStandingList = it
            _driverStandingList.addSource(it) { standings -> _driverStandingList.value = standings }
        }
        seasonConstructorStandingList?.let { _constructorStandingList.removeSource(it) }
        repository.getConstructorStandingsBySeason(season).also {
            seasonConstructorStandingList = it
            _constructorStandingList.addSource(it) { standings ->
                _constructorStandingList.value = standings
            }
        }
    }

    fun refreshDriverStandings(force: Boolean) {
        season?.let { season ->
            _driverStandingIsRefreshing.value = true
            viewModelScope.launch {
                _driverStandingRefreshResult.value =
                    repository.refreshDriverStandingsBySeason(season, force)
                _driverStandingIsRefreshing.value = false
            }
        }
    }

    fun clearDriverStandingRefreshResult() {
        _driverStandingRefreshResult.value = null
    }

    fun refreshConstructorStandings(force: Boolean) {
        season?.let { season ->
            _constructorStandingIsRefreshing.value = true
            viewModelScope.launch {
                _constructorStandingRefreshResult.value =
                    repository.refreshConstructorStandingsBySeason(season, force)
                _constructorStandingIsRefreshing.value = false
            }
        }
    }

    fun clearConstructorStandingRefreshResult() {
        _constructorStandingRefreshResult.value = null
    }

    override fun onCleared() {
        seasonDriverStandingList?.let { _driverStandingList.removeSource(it) }
        seasonDriverStandingList = null
        seasonConstructorStandingList?.let { _constructorStandingList.removeSource(it) }
        seasonConstructorStandingList = null
    }
}