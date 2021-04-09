package sam.frampton.parcferme.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import sam.frampton.parcferme.data.QualifyingResult
import sam.frampton.parcferme.data.RaceResult
import sam.frampton.parcferme.data.RefreshResult
import sam.frampton.parcferme.data.repositories.ResultRepository

class RaceDetailViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = ResultRepository()

    private val _networkError = MutableLiveData(false)
    val networkError: LiveData<Boolean>
        get() = _networkError
    private val _otherError = MutableLiveData(false)
    val otherError: LiveData<Boolean>
        get() = _otherError

    fun getRaceResults(season: Int, round: Int): LiveData<List<RaceResult>> {
        viewModelScope.launch {
            when (repository.refreshRaceResults(season, round)) {
                RefreshResult.NETWORK_ERROR -> _networkError.postValue(true)
                RefreshResult.OTHER_ERROR -> _otherError.postValue(true)
                RefreshResult.SUCCESS -> {
                }
            }
        }
        return repository.getRaceResults(season, round)
    }

    fun getQualifyingResults(season: Int, round: Int): LiveData<List<QualifyingResult>> {
        viewModelScope.launch {
            when (repository.refreshQualifyingResults(season, round)) {
                RefreshResult.NETWORK_ERROR -> _networkError.postValue(true)
                RefreshResult.OTHER_ERROR -> _otherError.postValue(true)
                RefreshResult.SUCCESS -> {
                }
            }
        }
        return repository.getQualifyingResults(season, round)
    }

    fun clearErrors() {
        _networkError.value = false
        _otherError.value = false
    }
}