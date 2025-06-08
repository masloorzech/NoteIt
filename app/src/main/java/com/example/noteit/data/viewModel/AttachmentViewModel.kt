package com.example.noteit.data.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.noteit.data.model.Attachment
import com.example.noteit.data.model.Task
import com.example.noteit.data.repository.AttachmentRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

class AttachmentViewModel(
    private val repository: AttachmentRepository
): ViewModel(){

    fun addAttachment(attachment: Attachment) {
        viewModelScope.launch {
            repository.insert(attachment)
        }
    }

    suspend fun addAttachmentSuspend(attachment: Attachment) {
        Log.d("ADD_ATTACHMENT", "Dodaję: $attachment")
        repository.insert(attachment)
    }

    fun getAttachmentsForTask(taskId: Int): Flow<List<Attachment>> {
        return repository.getAllAttachmentByTaskID(taskId)
    }

    fun deleteAttachment(attachment: Attachment) {
        viewModelScope.launch {
            repository.delete(attachment)
        }
    }

    fun deleteAttachmentsForTask(task: Task) {
        viewModelScope.launch {
            repository.deleteAttachmentsForTask(task.id)
        }
    }
}