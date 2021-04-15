package sam.frampton.parcferme.data.repositories

import android.content.Context
import sam.frampton.parcferme.R
import sam.frampton.parcferme.database.AppDatabase
import sam.frampton.parcferme.database.entities.DataTimestampEntity

class TimestampManager(val context: Context) {

    private val dataTimestampDao = AppDatabase.getInstance(context).dataTimestampDao()

    fun isCacheValid(dataKey: String, param: String = ""): Boolean {
        val cacheTimestamp = dataTimestampDao.getDataTimestamp(dataKey, param)?.timestamp ?: 0
        return (System.currentTimeMillis() - cacheTimestamp) <=
                context.resources.getInteger(R.integer.data_cache_ttl)
    }

    fun updateCacheTimestamp(dataKey: String, param: String = "") =
        dataTimestampDao.insertDataTimestamp(
            DataTimestampEntity(dataKey, param, System.currentTimeMillis())
        )
}