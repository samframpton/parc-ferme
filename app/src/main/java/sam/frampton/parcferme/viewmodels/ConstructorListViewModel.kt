package sam.frampton.parcferme.viewmodels

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import sam.frampton.parcferme.data.Constructor
import sam.frampton.parcferme.data.RefreshResult
import sam.frampton.parcferme.data.repositories.ConstructorRepository

class ConstructorListViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = ConstructorRepository(application)

    var season: Int? = null
        private set
    private val _constructorList = MediatorLiveData<List<Constructor>>()
    val constructorList: LiveData<List<Constructor>>
        get() = _constructorList
    private var seasonConstructorList: LiveData<List<Constructor>>? = null

    private val _refreshResult: MutableLiveData<RefreshResult> = MutableLiveData()
    val refreshResult: LiveData<RefreshResult>
        get() = _refreshResult
    private val _isRefreshing: MutableLiveData<Boolean> = MutableLiveData()
    val isRefreshing: LiveData<Boolean>
        get() = _isRefreshing

    fun setSeason(season: Int) {
        this.season = season
        seasonConstructorList?.let { _constructorList.removeSource(it) }
        repository.getConstructors(season).also {
            seasonConstructorList = it
            _constructorList.addSource(it) { constructors -> _constructorList.value = constructors }
        }
    }

    fun refreshConstructors(force: Boolean) {
        season?.let { season ->
            _isRefreshing.value = true
            viewModelScope.launch {
                _refreshResult.value = repository.refreshConstructors(season, force)
                _isRefreshing.value = false
            }
        }
    }

    fun clearRefreshResult() {
        _refreshResult.value = null
    }

    override fun onCleared() {
        seasonConstructorList?.let { _constructorList.removeSource(it) }
        seasonConstructorList = null
    }
}