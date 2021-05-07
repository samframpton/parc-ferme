package sam.frampton.parcferme.viewmodels

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import sam.frampton.parcferme.data.QualifyingResult
import sam.frampton.parcferme.data.Race
import sam.frampton.parcferme.data.RaceResult
import sam.frampton.parcferme.data.RefreshResult
import sam.frampton.parcferme.data.repositories.ResultRepository

class RaceDetailViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = ResultRepository(application)

    var race: Race? = null
        private set

    private val _raceResultList = MediatorLiveData<List<RaceResult>>()
    val raceResultList: LiveData<List<RaceResult>>
        get() = _raceResultList
    private var raceRaceResultList: LiveData<List<RaceResult>>? = null

    private val _raceResultRefreshResult: MutableLiveData<RefreshResult> = MutableLiveData()
    val raceResultRefreshResult: LiveData<RefreshResult>
        get() = _raceResultRefreshResult
    private val _raceResultIsRefreshing: MutableLiveData<Boolean> = MutableLiveData()
    val raceResultIsRefreshing: LiveData<Boolean>
        get() = _raceResultIsRefreshing

    private val _qualifyingResultList = MediatorLiveData<List<QualifyingResult>>()
    val qualifyingResultList: LiveData<List<QualifyingResult>>
        get() = _qualifyingResultList
    private var raceQualifyingResultList: LiveData<List<QualifyingResult>>? = null

    private val _qualifyingResultRefreshResult: MutableLiveData<RefreshResult> =
        MutableLiveData()
    val qualifyingResultRefreshResult: LiveData<RefreshResult>
        get() = _qualifyingResultRefreshResult
    private val _qualifyingResultIsRefreshing: MutableLiveData<Boolean> = MutableLiveData()
    val qualifyingResultIsRefreshing: LiveData<Boolean>
        get() = _qualifyingResultIsRefreshing

    fun setRace(race: Race) {
        this.race = race
        raceRaceResultList?.let { _raceResultList.removeSource(it) }
        repository.getRaceResults(race.season, race.round).also {
            raceRaceResultList = it
            _raceResultList.addSource(it) { results -> _raceResultList.value = results }
        }
        raceQualifyingResultList?.let { _qualifyingResultList.removeSource(it) }
        repository.getQualifyingResults(race.season, race.round).also {
            raceQualifyingResultList = it
            _qualifyingResultList.addSource(it) { results ->
                _qualifyingResultList.value = results
            }
        }
    }

    fun refreshRaceResults(force: Boolean) {
        race?.let { race ->
            _raceResultIsRefreshing.value = true
            viewModelScope.launch {
                _raceResultRefreshResult.value =
                    repository.refreshRaceResults(race.season, race.round, force)
                _raceResultIsRefreshing.value = false
            }
        }
    }

    fun clearRaceResultRefreshResult() {
        _raceResultRefreshResult.value = null
    }

    fun refreshQualifyingResults(force: Boolean) {
        race?.let { race ->
            _qualifyingResultIsRefreshing.value = true
            viewModelScope.launch {
                _qualifyingResultRefreshResult.value =
                    repository.refreshQualifyingResults(race.season, race.round, force)
                _qualifyingResultIsRefreshing.value = false
            }
        }
    }

    fun clearQualifyingResultRefreshResult() {
        _qualifyingResultRefreshResult.value = null
    }

    override fun onCleared() {
        raceRaceResultList?.let { _raceResultList.removeSource(it) }
        raceRaceResultList = null
        raceQualifyingResultList?.let { _qualifyingResultList.removeSource(it) }
        raceQualifyingResultList = null
    }
}