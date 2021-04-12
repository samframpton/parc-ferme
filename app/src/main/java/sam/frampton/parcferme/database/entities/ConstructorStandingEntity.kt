package sam.frampton.parcferme.database.entities

import androidx.room.*

@Entity(
    tableName = "constructor_standings",
    indices = [Index(value = ["constructorId", "season"], unique = true)],
    foreignKeys = [
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
data class ConstructorStandingEntity(
    @PrimaryKey(autoGenerate = true)
    val constructorStandingId: Long,
    val position: Int,
    val points: Double,
    val wins: Int,
    val constructorId: String,
    @ColumnInfo(index = true)
    val season: Int
)

data class ConstructorStandingAndConstructor(
    @Embedded
    val constructorStanding: ConstructorStandingEntity,
    @Relation(
        parentColumn = "constructorId",
        entityColumn = "constructorId"
    )
    val constructor: ConstructorEntity
)