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

    var title: String,
    var description: String,

    @ColumnInfo(name = "created_at") val createdAt: Long,
    @ColumnInfo(name = "due_at") var dueAt: Long,

    var isDone: Boolean = false,
    var hasNotification: Boolean = false,
    var hasAttachment : Boolean = false,

    @ColumnInfo(name = "category_id", index = true)
    var categoryId: Int? = null
)