package sam.frampton.parcferme.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import sam.frampton.parcferme.data.RefreshResult

class MainActivityViewModel : ViewModel() {

    private val _refreshResult: MutableLiveData<RefreshResult> = MutableLiveData()
    val refreshResult: LiveData<RefreshResult>
        get() = _refreshResult
    private val _menuRefresh: MutableLiveData<Boolean> = MutableLiveData()
    val menuRefresh: LiveData<Boolean>
        get() = _menuRefresh

    fun setRefreshResult(refreshResult: RefreshResult) {
        _refreshResult.value = refreshResult
    }

    fun clearRefreshResult() {
        _refreshResult.value = null
    }

    fun setMenuRefresh() {
        _menuRefresh.value = true
    }

    fun clearMenuRefresh() {
        _menuRefresh.value = false
    }
}