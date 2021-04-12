package sam.frampton.parcferme.data.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import sam.frampton.parcferme.database.entities.DriverEntity
import sam.frampton.parcferme.database.entities.SeasonDriverCrossRef
import sam.frampton.parcferme.database.entities.SeasonWithDrivers

@Dao
abstract class DriverDao {
    @Transaction
    @Query("SELECT * FROM seasons WHERE season = :season")
    abstract fun getDriversBySeason(season: Int): LiveData<SeasonWithDrivers>

    @Transaction
    open fun insertDriversBySeason(season: Int, drivers: List<DriverEntity>) {
        insertDrivers(drivers)
        insertSeasonDriverCrossRefs(
            drivers.map {
                SeasonDriverCrossRef(
                    season,
                    it.driverId
                )
            }
        )
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    protected abstract fun insertDrivers(drivers: List<DriverEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    protected abstract fun insertSeasonDriverCrossRefs(crossRefs: List<SeasonDriverCrossRef>)
}