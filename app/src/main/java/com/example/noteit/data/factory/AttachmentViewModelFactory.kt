package com.example.noteit.data.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.noteit.data.repository.AttachmentRepository
import com.example.noteit.data.viewModel.AttachmentViewModel

class AttachmentViewModelFactory(private val repository: AttachmentRepository): ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AttachmentViewModel::class.java)) {
            return AttachmentViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}