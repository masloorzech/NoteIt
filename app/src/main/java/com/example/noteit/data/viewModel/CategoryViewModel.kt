package com.example.noteit.data.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.noteit.data.model.Category
import com.example.noteit.data.repository.CategoryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.collections.*


class CategoryViewModel(private val repository: CategoryRepository) : ViewModel() {

    private val _categoryMap = MutableStateFlow<Map<Int, String>>(emptyMap())
    val categoryMap: StateFlow<Map<Int, String>> = _categoryMap

    val categories = repository.getAllCategories()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addCategory(category: Category) {
        viewModelScope.launch {
            repository.insert(category)
        }
    }

    fun deleteCategory(category: Category) {
        viewModelScope.launch {
            repository.delete(category)
        }
    }

    fun loadCategories() {
        viewModelScope.launch {
            repository.getAllCategories().collect { categories ->
                _categoryMap.value = categories.associateBy({ it.id }, { it.name })
            }
        }
    }
}
