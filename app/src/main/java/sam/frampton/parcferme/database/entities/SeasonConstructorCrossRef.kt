package sam.frampton.parcferme.database.entities

import androidx.room.*

@Entity(
    tableName = "season_constructor_cross_ref",
    primaryKeys = ["season", "constructorId"],
    foreignKeys = [
        ForeignKey(
            entity = SeasonEntity::class,
            parentColumns = ["season"],
            childColumns = ["season"]
        ),
        ForeignKey(
            entity = ConstructorEntity::class,
            parentColumns = ["constructorId"],
            childColumns = ["constructorId"]
        )
    ]
)
data class SeasonConstructorCrossRef(
    val season: Int,
    @ColumnInfo(index = true)
    val constructorId: String
)