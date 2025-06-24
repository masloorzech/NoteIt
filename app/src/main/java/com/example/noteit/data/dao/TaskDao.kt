package com.example.noteit.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.noteit.data.model.Attachment
import com.example.noteit.data.model.Task
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Query("SELECT * FROM tasks")
    fun getAllTasks(): Flow<List<Task>>

    @Query("SELECT * FROM tasks WHERE id = :taskId")
    suspend fun getTaskById(taskId: Int): Task?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(task: Task) : Long

    @Query("SELECT * FROM attachments WHERE id = :taskId")
    suspend fun getAttachmentsByTaskId(taskId: Int): List<Attachment>

    @Update
    suspend fun update(task: Task)

    @Delete
    suspend fun delete(task: Task)
}