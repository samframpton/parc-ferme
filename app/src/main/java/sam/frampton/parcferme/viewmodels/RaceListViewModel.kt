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

    private val _refreshResult: MutableLiveData<RefreshResult> = MutableLiveData()
    val refreshResult: LiveData<RefreshResult>
        get() = _refreshResult
    private val _isRefreshing: MutableLiveData<Boolean> = MutableLiveData()
    val isRefreshing: LiveData<Boolean>
        get() = _isRefreshing

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
            _isRefreshing.value = true
            viewModelScope.launch {
                _refreshResult.value = repository.refreshRaces(season, force)
                _isRefreshing.value = false
            }
        }
    }

    fun clearRefreshResult() {
        _refreshResult.value = null
    }

    override fun onCleared() {
        seasonRaceList?.let { _raceList.removeSource(it) }
        seasonRaceList = null
    }
}