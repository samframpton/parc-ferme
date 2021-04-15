package sam.frampton.parcferme.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import sam.frampton.parcferme.database.entities.DataTimestampEntity

@Dao
interface DataTimestampDao {
    @Query("SELECT * FROM data_timestamps WHERE dataKey = :dataKey AND param = :param")
    fun getDataTimestamp(dataKey: String, param: String): DataTimestampEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDataTimestamp(dataTimestamp: DataTimestampEntity)
}