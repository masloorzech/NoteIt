package com.example.noteit.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo

@Entity(tableName = "attachments")
data class Attachment(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,

    @ColumnInfo(name = "task_id", index = true)
    val taskId: Int,

    val filePath: String,
    val mimeType: String
)
