package ru.netology.nmedia.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface JobDao {
    @Query("SELECT * FROM jobs WHERE userId = :userId ORDER BY start DESC")
    fun getByUserId(userId: Long): Flow<List<JobEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(jobs: List<JobEntity>)

    @Query("DELETE FROM jobs WHERE userId = :userId")
    suspend fun clearByUserId(userId: Long)
}
