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
    open fun insertRaceResult(
        raceResult: RaceResultEntity,
        driver: DriverEntity,
        constructor: ConstructorEntity,
    ) {
        insertDriver(driver)
        insertConstructor(constructor)
        insertRaceResult(raceResult)
    }

    @Transaction
    open fun insertQualifyingResult(
        qualifyingResult: QualifyingResultEntity,
        driver: DriverEntity,
        constructor: ConstructorEntity
    ) {
        insertDriver(driver)
        insertConstructor(constructor)
        insertQualifyingResult(qualifyingResult)
    }

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    protected abstract fun insertDriver(driver: DriverEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    protected abstract fun insertConstructor(constructor: ConstructorEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    protected abstract fun insertRaceResult(raceResult: RaceResultEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    protected abstract fun insertQualifyingResult(qualifyingResult: QualifyingResultEntity)
}