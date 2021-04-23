package sam.frampton.parcferme.database.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import sam.frampton.parcferme.database.entities.ConstructorEntity
import sam.frampton.parcferme.database.entities.SeasonConstructorCrossRef
import sam.frampton.parcferme.database.entities.SeasonWithConstructors

@Dao
abstract class ConstructorDao {
    @Transaction
    @Query("SELECT * FROM seasons WHERE season = :season")
    abstract fun getConstructorsBySeason(season: Int): LiveData<SeasonWithConstructors>

    @Transaction
    open fun insertConstructorsBySeason(season: Int, constructors: List<ConstructorEntity>) {
        insertConstructors(constructors)
        insertSeasonConstructorCrossRefs(
            constructors.map { SeasonConstructorCrossRef(season, it.constructorId) }
        )
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    protected abstract fun insertConstructors(constructors: List<ConstructorEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    protected abstract fun insertSeasonConstructorCrossRefs(
        crossRefs: List<SeasonConstructorCrossRef>
    )
}