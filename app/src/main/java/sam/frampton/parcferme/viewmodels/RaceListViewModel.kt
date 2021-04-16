package sam.frampton.parcferme.viewmodels

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import sam.frampton.parcferme.data.Race
import sam.frampton.parcferme.data.RefreshResult
import sam.frampton.parcferme.data.repositories.RaceRepository

class RaceListViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = RaceRepository(application)

    var season: Int? = null
        private set
    private val _raceList = MediatorLiveData<List<Race>>()
    val raceList: LiveData<List<Race>>
        get() = _raceList
    private var seasonRaceList: LiveData<List<Race>>? = null

    private val _networkError = MutableLiveData(false)
    val networkError: LiveData<Boolean>
        get() = _networkError
    private val _otherError = MutableLiveData(false)
    val otherError: LiveData<Boolean>
        get() = _otherError

    fun setSeason(season: Int) {
        this.season = season
        seasonRaceList?.let { _raceList.removeSource(it) }
        repository.getRaces(season).also {
            seasonRaceList = it
            _raceList.addSource(it) { races -> _raceList.value = races }
        }
    }

    fun refreshRaces(force: Boolean) {
        season?.let { season ->
            viewModelScope.launch {
                when (repository.refreshRaces(season, force)) {
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
        seasonRaceList?.let { _raceList.removeSource(it) }
        seasonRaceList = null
    }
}