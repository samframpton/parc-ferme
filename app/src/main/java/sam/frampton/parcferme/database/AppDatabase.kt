package sam.frampton.parcferme.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import sam.frampton.parcferme.database.daos.*
import sam.frampton.parcferme.database.entities.*

@Database(
    entities = [
        DataTimestampEntity::class,
        SeasonEntity::class,
        DriverEntity::class,
        SeasonDriverCrossRef::class,
        ConstructorEntity::class,
        SeasonConstructorCrossRef::class,
        RaceEntity::class,
        CircuitEntity::class,
        RaceResultEntity::class,
        QualifyingResultEntity::class,
        DriverStandingEntity::class,
        ConstructorStandingEntity::class,
        DriverStandingConstructorCrossRef::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun dataTimestampDao(): DataTimestampDao

    abstract fun seasonDao(): SeasonDao

    abstract fun driverDao(): DriverDao

    abstract fun constructorDao(): ConstructorDao

    abstract fun raceDao(): RaceDao

    abstract fun resultDao(): ResultDao

    abstract fun standingDao(): StandingDao

    companion object {
        private const val DATABASE_NAME = "parcferme-db"

        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context, AppDatabase::class.java, DATABASE_NAME
                )
                    .build().also { instance = it }
            }
        }
    }
}