package sam.frampton.parcferme.database.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import sam.frampton.parcferme.database.entities.*

@Dao
abstract class StandingDao {
    @Transaction
    @Query("SELECT * FROM driver_standings WHERE season = :season ORDER BY position ASC")
    abstract fun getDriverStandingsBySeason(season: Int):
            LiveData<List<DriverStandingWithDriverConstructors>>

    @Transaction
    @Query("SELECT * FROM driver_standings WHERE driverId = :driverId ORDER BY season DESC")
    abstract fun getDriverStandingsByDriver(driverId: String):
            LiveData<List<DriverStandingWithDriverConstructors>>

    @Transaction
    @Query("SELECT * FROM constructor_standings WHERE season = :season ORDER BY position ASC")
    abstract fun getConstructorStandingsBySeason(season: Int):
            LiveData<List<ConstructorStandingAndConstructor>>

    @Transaction
    @Query("SELECT * FROM constructor_standings WHERE constructorId = :constructorId ORDER BY season DESC")
    abstract fun getConstructorStandingsByConstructor(constructorId: String):
            LiveData<List<ConstructorStandingAndConstructor>>

    @Transaction
    open fun insertDriverStandings(
        driverStandings: List<Pair<DriverStandingEntity, List<ConstructorEntity>>>,
        drivers: List<DriverEntity>
    ) {
        insertDrivers(drivers)
        driverStandings.forEach { pair ->
            val driverStandingId = insertDriverStanding(pair.first)
            insertConstructors(pair.second)
            pair.second.forEach {
                insertDriverStandingConstructorCrossRef(
                    DriverStandingConstructorCrossRef(driverStandingId, it.constructorId)
                )
            }
        }
    }

    @Transaction
    open fun insertConstructorStandings(
        constructorStandings: List<ConstructorStandingEntity>,
        constructors: List<ConstructorEntity>
    ) {
        insertConstructors(constructors)
        insertConstructorStandings(constructorStandings)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    protected abstract fun insertDriverStanding(driverStanding: DriverStandingEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    protected abstract fun insertDriverStandingConstructorCrossRef(
        crossRef: DriverStandingConstructorCrossRef
    )

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    protected abstract fun insertConstructorStandings(
        constructorStandings: List<ConstructorStandingEntity>
    )

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    protected abstract fun insertDrivers(drivers: List<DriverEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    protected abstract fun insertConstructors(constructors: List<ConstructorEntity>)
}