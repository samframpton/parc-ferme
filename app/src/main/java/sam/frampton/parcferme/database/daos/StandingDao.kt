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
    open fun insertDriverStanding(
        driverStanding: DriverStandingEntity,
        driver: DriverEntity,
        constructors: List<ConstructorEntity>
    ) {
        insertDriver(driver)
        val driverStandingId = insertDriverStanding(driverStanding)
        constructors.forEach {
            insertConstructor(it)
            insertDriverStandingConstructorCrossRef(
                DriverStandingConstructorCrossRef(driverStandingId, it.constructorId)
            )
        }
    }

    @Transaction
    open fun insertConstructorStanding(
        constructorStanding: ConstructorStandingEntity,
        constructor: ConstructorEntity
    ) {
        insertConstructor(constructor)
        insertConstructorStanding(constructorStanding)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    protected abstract fun insertDriverStanding(driverStanding: DriverStandingEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    protected abstract fun insertDriverStandingConstructorCrossRef(
        crossRef: DriverStandingConstructorCrossRef
    )

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    protected abstract fun insertConstructorStanding(constructorStanding: ConstructorStandingEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    protected abstract fun insertDriver(driver: DriverEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    protected abstract fun insertConstructor(constructor: ConstructorEntity)
}