package sam.frampton.parcferme.database.entities

import androidx.room.*

@Entity(
    tableName = "driver_standings",
    indices = [Index(value = ["driverId", "season"], unique = true)],
    foreignKeys = [
        ForeignKey(
            entity = DriverEntity::class,
            parentColumns = ["driverId"],
            childColumns = ["driverId"]
        ),
        ForeignKey(
            entity = SeasonEntity::class,
            parentColumns = ["season"],
            childColumns = ["season"]
        )
    ]
)
data class DriverStandingEntity(
    @PrimaryKey(autoGenerate = true)
    val driverStandingId: Long,
    val position: Int,
    val points: Double,
    val wins: Int,
    val driverId: String,
    @ColumnInfo(index = true)
    val season: Int
)

data class DriverStandingWithDriverConstructors(
    @Embedded
    val driverStanding: DriverStandingEntity,
    @Relation(
        parentColumn = "driverId",
        entityColumn = "driverId"
    )
    val driver: DriverEntity,
    @Relation(
        parentColumn = "driverStandingId",
        entityColumn = "constructorId",
        associateBy = Junction(DriverStandingConstructorCrossRef::class)
    )
    val constructors: List<ConstructorEntity>
)