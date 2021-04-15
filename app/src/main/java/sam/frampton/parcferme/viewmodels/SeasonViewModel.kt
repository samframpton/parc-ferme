package sam.frampton.parcferme.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import sam.frampton.parcferme.data.RefreshResult
import sam.frampton.parcferme.data.repositories.SeasonRepository

class SeasonViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = SeasonRepository(application)

    val seasons = repository.getSeasons()

    private val _networkError = MutableLiveData(false)
    val networkError: LiveData<Boolean>
        get() = _networkError
    private val _otherError = MutableLiveData(false)
    val otherError: LiveData<Boolean>
        get() = _otherError

    init {
        viewModelScope.launch {
            when (repository.refreshSeasons()) {
                RefreshResult.NETWORK_ERROR -> _networkError.postValue(true)
                RefreshResult.OTHER_ERROR -> _otherError.postValue(true)
                RefreshResult.SUCCESS -> {
                }
                RefreshResult.CACHE -> {
                }
            }
        }
    }

    fun clearErrors() {
        _networkError.value = false
        _otherError.value = false
    }
}