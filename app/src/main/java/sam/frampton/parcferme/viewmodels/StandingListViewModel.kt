package sam.frampton.parcferme.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import sam.frampton.parcferme.data.ConstructorStanding
import sam.frampton.parcferme.data.DriverStanding
import sam.frampton.parcferme.data.RefreshResult
import sam.frampton.parcferme.data.repositories.StandingRepository

class StandingListViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = StandingRepository(application)

    private val _networkError = MutableLiveData(false)
    val networkError: LiveData<Boolean>
        get() = _networkError
    private val _otherError = MutableLiveData(false)
    val otherError: LiveData<Boolean>
        get() = _otherError

    fun getDriverStandings(season: Int): LiveData<List<DriverStanding>> {
        viewModelScope.launch {
            when (repository.refreshDriverStandingsBySeason(season)) {
                RefreshResult.NETWORK_ERROR -> _networkError.postValue(true)
                RefreshResult.OTHER_ERROR -> _otherError.postValue(true)
                RefreshResult.SUCCESS -> {
                }
                RefreshResult.CACHE -> {
                }
            }
        }
        return repository.getDriverStandingsBySeason(season)
    }

    fun getConstructorStandings(season: Int): LiveData<List<ConstructorStanding>> {
        viewModelScope.launch {
            when (repository.refreshConstructorStandingsBySeason(season)) {
                RefreshResult.NETWORK_ERROR -> _networkError.postValue(true)
                RefreshResult.OTHER_ERROR -> _otherError.postValue(true)
                RefreshResult.SUCCESS -> {
                }
                RefreshResult.CACHE -> {
                }
            }
        }
        return repository.getConstructorStandingsBySeason(season)
    }

    fun clearErrors() {
        _networkError.value = false
        _otherError.value = false
    }
}