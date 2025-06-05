package com.example.noteit.data.repository

import com.example.noteit.data.dao.CategoryDao
import com.example.noteit.data.model.Category
import kotlinx.coroutines.flow.Flow

class CategoryRepository(private val categoryDao: CategoryDao) {

    fun getAllCategories(): Flow<List<Category>> = categoryDao.getAllCategories()

    suspend fun insert(category: Category) = categoryDao.insert(category)

    suspend fun delete(category: Category) = categoryDao.delete(category)

    suspend fun getCategoryById(id: Int): Category? = categoryDao.getCategoryById(id)
}
