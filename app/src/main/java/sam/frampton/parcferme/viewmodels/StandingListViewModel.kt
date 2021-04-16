package sam.frampton.parcferme.viewmodels

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import sam.frampton.parcferme.data.ConstructorStanding
import sam.frampton.parcferme.data.DriverStanding
import sam.frampton.parcferme.data.RefreshResult
import sam.frampton.parcferme.data.repositories.StandingRepository
import sam.frampton.parcferme.fragments.StandingListFragment

class StandingListViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = StandingRepository(application)

    var season: Int? = null
        private set
    var standingType: StandingListFragment.StandingType = StandingListFragment.StandingType.DEFAULT
    private val _driverStandingList = MediatorLiveData<List<DriverStanding>>()
    val driverStandingList: LiveData<List<DriverStanding>>
        get() = _driverStandingList
    private var seasonDriverStandingList: LiveData<List<DriverStanding>>? = null
    private val _constructorStandingList = MediatorLiveData<List<ConstructorStanding>>()
    val constructorStandingList: LiveData<List<ConstructorStanding>>
        get() = _constructorStandingList
    private var seasonConstructorStandingList: LiveData<List<ConstructorStanding>>? = null

    private val _networkError = MutableLiveData(false)
    val networkError: LiveData<Boolean>
        get() = _networkError
    private val _otherError = MutableLiveData(false)
    val otherError: LiveData<Boolean>
        get() = _otherError

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
            viewModelScope.launch {
                when (repository.refreshDriverStandingsBySeason(season, force)) {
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

    fun refreshConstructorStandings(force: Boolean) {
        season?.let { season ->
            viewModelScope.launch {
                when (repository.refreshConstructorStandingsBySeason(season, force)) {
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
        seasonDriverStandingList?.let { _driverStandingList.removeSource(it) }
        seasonDriverStandingList = null
        seasonConstructorStandingList?.let { _constructorStandingList.removeSource(it) }
        seasonConstructorStandingList = null
    }
}