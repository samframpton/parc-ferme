package sam.frampton.parcferme.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import sam.frampton.parcferme.data.Constructor
import sam.frampton.parcferme.data.ConstructorStanding
import sam.frampton.parcferme.data.RefreshResult
import sam.frampton.parcferme.data.repositories.StandingRepository

class ConstructorDetailViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = StandingRepository(application)

    private val _networkError = MutableLiveData(false)
    val networkError: LiveData<Boolean>
        get() = _networkError
    private val _otherError = MutableLiveData(false)
    val otherError: LiveData<Boolean>
        get() = _otherError

    fun getConstructorStandings(constructor: Constructor): LiveData<List<ConstructorStanding>> {
        viewModelScope.launch {
            when (repository.refreshConstructorStandingsByConstructor(constructor.constructorId)) {
                RefreshResult.NETWORK_ERROR -> _networkError.postValue(true)
                RefreshResult.OTHER_ERROR -> _otherError.postValue(true)
                RefreshResult.SUCCESS -> {
                }
            }
        }
        return repository.getConstructorStandingsByConstructor(constructor.constructorId)
    }

    fun clearErrors() {
        _networkError.value = false
        _otherError.value = false
    }
}