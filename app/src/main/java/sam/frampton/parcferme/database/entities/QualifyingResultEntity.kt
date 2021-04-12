package sam.frampton.parcferme.database.entities

import androidx.room.*

@Entity(
    tableName = "qualifying_results",
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
data class QualifyingResultEntity(
    @PrimaryKey(autoGenerate = true)
    val qualifyingResultId: Long,
    val number: Int,
    val position: Int,
    @ColumnInfo(index = true)
    val driverId: String,
    @ColumnInfo(index = true)
    val constructorId: String,
    val q1: String?,
    val q2: String?,
    val q3: String?,
    val season: Int,
    val round: Int
)

data class QualifyingResultAndDriverConstructor(
    @Embedded
    val qualifyingResult: QualifyingResultEntity,
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