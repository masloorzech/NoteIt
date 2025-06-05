package com.example.noteit.data.dao

import androidx.room.*
import com.example.noteit.data.model.Attachment

@Dao
interface AttachmentDao {

    @Query("SELECT * FROM attachments WHERE task_id = :taskId")
    suspend fun getAttachmentsForTask(taskId: Int): List<Attachment>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(attachment: Attachment)

    @Delete
    suspend fun delete(attachment: Attachment)
}
