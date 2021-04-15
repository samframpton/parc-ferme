package sam.frampton.parcferme.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "data_timestamps", primaryKeys = ["dataKey", "param"])
data class DataTimestampEntity(
    val dataKey: String,
    @ColumnInfo(index = true)
    val param: String,
    val timestamp: Long
)