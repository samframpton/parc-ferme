package sam.frampton.parcferme.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import sam.frampton.parcferme.data.DriverStanding
import sam.frampton.parcferme.data.RefreshResult
import sam.frampton.parcferme.data.repositories.StandingRepository

class DriverDetailViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = StandingRepository(application)

    private val _networkError = MutableLiveData(false)
    val networkError: LiveData<Boolean>
        get() = _networkError
    private val _otherError = MutableLiveData(false)
    val otherError: LiveData<Boolean>
        get() = _otherError

    fun getDriverStandings(driverId: String): LiveData<List<DriverStanding>> {
        viewModelScope.launch {
            when (repository.refreshDriverStandingsByDriver(driverId)) {
                RefreshResult.NETWORK_ERROR -> _networkError.postValue(true)
                RefreshResult.OTHER_ERROR -> _otherError.postValue(true)
                RefreshResult.SUCCESS -> {
                }
            }
        }
        return repository.getDriverStandingsByDriver(driverId)
    }

    fun clearErrors() {
        _networkError.value = false
        _otherError.value = false
    }
}