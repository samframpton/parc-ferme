package sam.frampton.parcferme.database.entities

import androidx.room.*

@Entity(
    tableName = "race_results",
    indices = [Index(value = ["season", "round", "driverId"], unique = true)],
    foreignKeys = [
        ForeignKey(
            entity = DriverEntity::class,
            parentColumns = ["driverId"],
            childColumns = ["driverId"]
        ),
        ForeignKey(
            entity = ConstructorEntity::class,
            parentColumns = ["constructorId"],
            childColumns = ["constructorId"]
        ),
        ForeignKey(
            entity = SeasonEntity::class,
            parentColumns = ["season"],
            childColumns = ["season"]
        )
    ]
)
data class RaceResultEntity(
    @PrimaryKey(autoGenerate = true)
    val raceResultId: Long,
    val number: Int,
    val position: Int,
    val positionText: String,
    val points: Double,
    @ColumnInfo(index = true)
    val driverId: String,
    @ColumnInfo(index = true)
    val constructorId: String,
    val grid: Int,
    val laps: Int,
    val status: String,
    val time: String?,
    val millis: Int?,
    val fastestLap: Int?,
    val fastestLapRank: Int?,
    val fastestLapTime: String?,
    val fastestLapAverageSpeed: Double?,
    val fastestLapAverageSpeedUnits: String?,
    val season: Int,
    val round: Int
)

data class RaceResultAndDriverConstructor(
    @Embedded
    val raceResult: RaceResultEntity,
    @Relation(
        parentColumn = "driverId",
        entityColumn = "driverId"
    )
    val driver: DriverEntity,
    @Relation(
        parentColumn = "constructorId",
        entityColumn = "constructorId"
    )
    val constructor: ConstructorEntity
)