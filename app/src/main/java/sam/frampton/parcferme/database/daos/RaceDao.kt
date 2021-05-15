package sam.frampton.parcferme.database.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import sam.frampton.parcferme.database.entities.CircuitEntity
import sam.frampton.parcferme.database.entities.RaceAndCircuit
import sam.frampton.parcferme.database.entities.RaceEntity

@Dao
abstract class RaceDao {
    @Transaction
    @Query("SELECT * FROM races WHERE season = :season")
    abstract fun getRacesBySeason(season: Int): LiveData<List<RaceAndCircuit>>

    @Transaction
    open fun insertRaces(races: List<RaceEntity>, circuits: List<CircuitEntity>) {
        insertCircuits(circuits)
        insertRaces(races)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    protected abstract fun insertCircuits(circuits: List<CircuitEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    protected abstract fun insertRaces(races: List<RaceEntity>)
}