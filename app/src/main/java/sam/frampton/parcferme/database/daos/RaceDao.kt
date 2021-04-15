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
    open fun insertRace(race: RaceEntity, circuit: CircuitEntity) {
        insertCircuit(circuit)
        insertRace(race)
    }

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    protected abstract fun insertCircuit(circuit: CircuitEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    protected abstract fun insertRace(race: RaceEntity)
}