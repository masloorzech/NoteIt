package com.example.noteit.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.noteit.data.model.Task
import com.example.noteit.data.model.Category
import com.example.noteit.data.model.Attachment
import com.example.noteit.data.dao.TaskDao
import com.example.noteit.data.dao.CategoryDao
import com.example.noteit.data.dao.AttachmentDao

@Database(
    entities = [Task::class, Category::class, Attachment::class],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun taskDao(): TaskDao
    abstract fun categoryDao(): CategoryDao
    abstract fun attachmentDao(): AttachmentDao
}
