package sam.frampton.parcferme.database.entities

import androidx.room.*

@Entity(tableName = "seasons")
data class SeasonEntity(
    @PrimaryKey
    val season: Int,
    val url: String?
)

data class SeasonWithDrivers(
    @Embedded
    val season: SeasonEntity,
    @Relation(
        parentColumn = "season",
        entityColumn = "driverId",
        associateBy = Junction(SeasonDriverCrossRef::class)
    )
    val drivers: List<DriverEntity>
)

class SeasonWithConstructors(
    @Embedded
    val season: SeasonEntity,
    @Relation(
        parentColumn = "season",
        entityColumn = "constructorId",
        associateBy = Junction(SeasonConstructorCrossRef::class)
    )
    val constructors: List<ConstructorEntity>
)