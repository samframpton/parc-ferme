package sam.frampton.parcferme.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "circuits")
data class CircuitEntity(
    @PrimaryKey
    val circuitId: String,
    val circuitName: String,
    val locality: String,
    val country: String,
    val latitude: Double,
    val longitude: Double,
    val url: String?
)