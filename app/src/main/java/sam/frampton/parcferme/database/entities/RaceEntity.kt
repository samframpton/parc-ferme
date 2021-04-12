package sam.frampton.parcferme.database.entities

import androidx.room.*

@Entity(
    tableName = "races",
    indices = [Index(value = ["season", "round"], unique = true)],
    foreignKeys = [
        ForeignKey(
            entity = CircuitEntity::class,
            parentColumns = ["circuitId"],
            childColumns = ["circuitId"]
        ),
        ForeignKey(
            entity = SeasonEntity::class,
            parentColumns = ["season"],
            childColumns = ["season"]
        )
    ],
)
data class RaceEntity(
    @PrimaryKey(autoGenerate = true)
    val raceId: Long,
    val season: Int,
    val round: Int,
    val raceName: String,
    @ColumnInfo(index = true)
    val circuitId: String,
    val date: String,
    val time: String?,
    val url: String?,
)

data class RaceAndCircuit(
    @Embedded
    val race: RaceEntity,
    @Relation(
        parentColumn = "circuitId",
        entityColumn = "circuitId"
    )
    val circuit: CircuitEntity
)