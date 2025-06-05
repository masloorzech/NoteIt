package com.example.noteit.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.TypeConverters
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "tasks",
    foreignKeys = [ForeignKey(
        entity = Category::class,
        parentColumns = ["id"],
        childColumns = ["category_id"],
        onDelete = ForeignKey.SET_NULL
    )]
)

data class Task(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,

    val title: String,
    val description: String,

    @ColumnInfo(name = "created_at") val createdAt: Long,
    @ColumnInfo(name = "due_at") val dueAt: Long,

    var isDone: Boolean = false,
    val hasNotification: Boolean = true,

    @ColumnInfo(name = "attachment_id", index = true)
    val attachmentId: Int? = null,

    @ColumnInfo(name = "category_id", index = true)
    val categoryId: Int? = null
)