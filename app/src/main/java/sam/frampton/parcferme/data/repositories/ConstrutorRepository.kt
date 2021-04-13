package sam.frampton.parcferme.data.repositories

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import sam.frampton.parcferme.api.ErgastService
import sam.frampton.parcferme.api.dtos.ErgastResponse
import sam.frampton.parcferme.data.Constructor
import sam.frampton.parcferme.data.RefreshResult
import sam.frampton.parcferme.data.toConstructorEntityList
import sam.frampton.parcferme.data.toConstructorList
import sam.frampton.parcferme.database.AppDatabase
import java.io.IOException

class ConstructorRepository(context: Context) {

    private val constructorDao = AppDatabase.getInstance(context).constructorDao()

    fun getConstructors(season: Int): LiveData<List<Constructor>> =
        Transformations.map(constructorDao.getConstructorsBySeason(season)) {
            it.constructors.toConstructorList()
        }

    suspend fun refreshConstructors(season: Int): RefreshResult =
        withContext(Dispatchers.IO) {
            try {
                val apiResponse = ErgastService.instance.constructors(season)
                cacheApiConstructors(apiResponse)
            } catch (throwable: Throwable) {
                when (throwable) {
                    is IOException -> RefreshResult.NETWORK_ERROR
                    else -> RefreshResult.OTHER_ERROR
                }
            }
        }

    private fun cacheApiConstructors(response: ErgastResponse): RefreshResult =
        response.motorRacingData.constructorTable?.let { constructorTable ->
            constructorTable.season?.let {
                constructorDao.insertConstructorsBySeason(
                    it,
                    constructorTable.constructors.toConstructorEntityList()
                )
                RefreshResult.SUCCESS
            }
        } ?: RefreshResult.OTHER_ERROR
}