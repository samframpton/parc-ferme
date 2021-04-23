package sam.frampton.parcferme.database.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import sam.frampton.parcferme.database.entities.SeasonEntity

@Dao
interface SeasonDao {
    @Query("SELECT * FROM seasons ORDER BY season DESC")
    fun getSeasons(): LiveData<List<SeasonEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSeasons(seasons: List<SeasonEntity>)
}