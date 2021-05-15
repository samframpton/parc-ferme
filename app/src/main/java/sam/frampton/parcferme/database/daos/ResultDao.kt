package sam.frampton.parcferme.database.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import sam.frampton.parcferme.database.entities.*

@Dao
abstract class ResultDao {
    @Transaction
    @Query("SELECT * FROM race_results WHERE season = :season AND round = :round ORDER BY position ASC")
    abstract fun getRaceResultsBySeasonRound(season: Int, round: Int):
            LiveData<List<RaceResultAndDriverConstructor>>

    @Transaction
    @Query("SELECT * FROM qualifying_results WHERE season = :season AND round = :round ORDER BY position ASC")
    abstract fun getQualifyingResultsBySeasonRound(season: Int, round: Int):
            LiveData<List<QualifyingResultAndDriverConstructor>>

    @Transaction
    open fun insertRaceResults(
        raceResults: List<RaceResultEntity>,
        drivers: List<DriverEntity>,
        constructors: List<ConstructorEntity>
    ) {
        insertDrivers(drivers)
        insertConstructors(constructors)
        insertRaceResults(raceResults)
    }

    @Transaction
    open fun insertQualifyingResults(
        qualifyingResults: List<QualifyingResultEntity>,
        drivers: List<DriverEntity>,
        constructors: List<ConstructorEntity>
    ) {
        insertDrivers(drivers)
        insertConstructors(constructors)
        insertQualifyingResults(qualifyingResults)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    protected abstract fun insertDrivers(drivers: List<DriverEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    protected abstract fun insertConstructors(constructors: List<ConstructorEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    protected abstract fun insertRaceResults(raceResults: List<RaceResultEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    protected abstract fun insertQualifyingResults(qualifyingResults: List<QualifyingResultEntity>)
}