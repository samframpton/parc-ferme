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

    private val _refreshResult: MutableLiveData<RefreshResult> = MutableLiveData()
    val refreshResult: LiveData<RefreshResult>
        get() = _refreshResult
    private val _isRefreshing: MutableLiveData<Boolean> = MutableLiveData()
    val isRefreshing: LiveData<Boolean>
        get() = _isRefreshing

    init {
        refreshSeasons(false)
    }

    fun refreshSeasons(force: Boolean) {
        _isRefreshing.value = true
        viewModelScope.launch {
            _refreshResult.value = repository.refreshSeasons(force)
            _isRefreshing.value = false
        }
    }

    fun clearRefreshResult() {
        _refreshResult.value = null
    }
}