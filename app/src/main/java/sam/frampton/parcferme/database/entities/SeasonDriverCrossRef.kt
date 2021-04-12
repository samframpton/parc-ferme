package sam.frampton.parcferme.database.entities

import androidx.room.*

@Entity(
    tableName = "season_driver_cross_ref",
    primaryKeys = ["season", "driverId"],
    foreignKeys = [
        ForeignKey(
            entity = SeasonEntity::class,
            parentColumns = ["season"],
            childColumns = ["season"]
        ),
        ForeignKey(
            entity = DriverEntity::class,
            parentColumns = ["driverId"],
            childColumns = ["driverId"]
        )
    ]
)
data class SeasonDriverCrossRef(
    val season: Int,
    @ColumnInfo(index = true)
    val driverId: String
)