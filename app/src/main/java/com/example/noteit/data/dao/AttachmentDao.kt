package com.example.noteit.data.dao

import androidx.room.*
import com.example.noteit.data.model.Attachment
import kotlinx.coroutines.flow.Flow

@Dao
interface AttachmentDao {

    @Query("SELECT * FROM attachments")
    fun getAllAttachments(): Flow<List<Attachment>>

    @Query("SELECT * FROM attachments WHERE task_id = :taskId")
    fun getAttachmentsForTask(taskId: Int): Flow<List<Attachment>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(attachment: Attachment)

    @Delete
    suspend fun delete(attachment: Attachment)

    @Query("DELETE FROM attachments WHERE task_id = :taskId")
    suspend fun deleteAttachmentsForTask(taskId: Int)
}
