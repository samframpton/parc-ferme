package sam.frampton.parcferme.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "driver_standing_constructor_cross_ref",
    primaryKeys = ["driverStandingId", "constructorId"],
    foreignKeys = [
        ForeignKey(
            entity = DriverStandingEntity::class,
            parentColumns = ["driverStandingId"],
            childColumns = ["driverStandingId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ConstructorEntity::class,
            parentColumns = ["constructorId"],
            childColumns = ["constructorId"]
        )
    ]
)
data class DriverStandingConstructorCrossRef(
    val driverStandingId: Long,
    @ColumnInfo(index = true)
    val constructorId: String
)