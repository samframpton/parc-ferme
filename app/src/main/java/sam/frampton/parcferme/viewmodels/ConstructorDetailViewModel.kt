package sam.frampton.parcferme.viewmodels

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import sam.frampton.parcferme.data.Constructor
import sam.frampton.parcferme.data.ConstructorStanding
import sam.frampton.parcferme.data.RefreshResult
import sam.frampton.parcferme.data.repositories.StandingRepository

class ConstructorDetailViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = StandingRepository(application)

    var constructor: Constructor? = null
        private set
    private val _standingList = MediatorLiveData<List<ConstructorStanding>>()
    val standingList: LiveData<List<ConstructorStanding>>
        get() = _standingList
    private var constructorStandingList: LiveData<List<ConstructorStanding>>? = null

    private val _refreshResult: MutableLiveData<RefreshResult> = MutableLiveData()
    val refreshResult: LiveData<RefreshResult>
        get() = _refreshResult
    private val _isRefreshing: MutableLiveData<Boolean> = MutableLiveData()
    val isRefreshing: LiveData<Boolean>
        get() = _isRefreshing

    fun setConstructor(constructor: Constructor) {
        this.constructor = constructor
        constructorStandingList?.let { _standingList.removeSource(it) }
        repository.getConstructorStandingsByConstructor(constructor.constructorId).also {
            constructorStandingList = it
            _standingList.addSource(it) { standings -> _standingList.value = standings }
        }
    }

    fun refreshConstructorStandings(force: Boolean) {
        constructor?.let { constructor ->
            _isRefreshing.value = true
            viewModelScope.launch {
                _refreshResult.value = repository.refreshConstructorStandingsByConstructor(
                    constructor.constructorId,
                    force
                )
                _isRefreshing.value = false
            }
        }
    }

    fun clearRefreshResult() {
        _refreshResult.value = null
    }

    override fun onCleared() {
        constructorStandingList?.let { _standingList.removeSource(it) }
        constructorStandingList = null
    }
}