package sam.frampton.parcferme.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import sam.frampton.parcferme.data.Driver
import sam.frampton.parcferme.data.RefreshResult
import sam.frampton.parcferme.data.repositories.DriverRepository

class DriverListViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = DriverRepository(application)

    private val _networkError = MutableLiveData(false)
    val networkError: LiveData<Boolean>
        get() = _networkError
    private val _otherError = MutableLiveData(false)
    val otherError: LiveData<Boolean>
        get() = _otherError

    fun getDrivers(season: Int): LiveData<List<Driver>> {
        viewModelScope.launch {
            when (repository.refreshDrivers(season)) {
                RefreshResult.NETWORK_ERROR -> _networkError.postValue(true)
                RefreshResult.OTHER_ERROR -> _otherError.postValue(true)
                RefreshResult.SUCCESS -> {
                }
                RefreshResult.CACHE -> {
                }
            }
        }
        return repository.getDrivers(season)
    }

    fun clearErrors() {
        _networkError.value = false
        _otherError.value = false
    }
}