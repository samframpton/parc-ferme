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

    private val _networkError = MutableLiveData(false)
    val networkError: LiveData<Boolean>
        get() = _networkError
    private val _otherError = MutableLiveData(false)
    val otherError: LiveData<Boolean>
        get() = _otherError

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
            viewModelScope.launch {
                when (repository.refreshConstructors(season, force)) {
                    RefreshResult.NETWORK_ERROR -> _networkError.postValue(true)
                    RefreshResult.OTHER_ERROR -> _otherError.postValue(true)
                    RefreshResult.SUCCESS -> {
                    }
                    RefreshResult.CACHE -> {
                    }
                }
            }
        }
    }

    fun clearErrors() {
        _networkError.value = false
        _otherError.value = false
    }

    override fun onCleared() {
        seasonConstructorList?.let { _constructorList.removeSource(it) }
        seasonConstructorList = null
    }
}