package com.example.noteit.data.repository

import com.example.noteit.data.dao.AttachmentDao
import com.example.noteit.data.model.Attachment
import kotlinx.coroutines.flow.Flow

class AttachmentRepository(private val attatchmentDao: AttachmentDao) {

    fun getAllAttachment(): Flow<List<Attachment>> = attatchmentDao.getAllAttachments()

    fun getAllAttachmentByTaskID(taskId: Int): Flow<List<Attachment>> = attatchmentDao.getAttachmentsForTask(taskId)

    suspend fun insert(attatchment: Attachment) = attatchmentDao.insert(attatchment)

    suspend fun delete(attatchment: Attachment) = attatchmentDao.delete(attatchment)

    suspend fun deleteAttachmentsForTask(taskId: Int) = attatchmentDao.deleteAttachmentsForTask(taskId)

}