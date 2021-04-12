package sam.frampton.parcferme.data.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import sam.frampton.parcferme.database.entities.SeasonEntity

@Dao
interface SeasonDao {
    @Query("SELECT * FROM seasons ORDER BY season DESC")
    fun getSeasons(): LiveData<List<SeasonEntity>>

    @Insert
    fun insertSeasons(seasons: List<SeasonEntity>)
}