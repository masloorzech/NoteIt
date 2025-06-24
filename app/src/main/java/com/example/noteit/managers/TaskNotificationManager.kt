package com.example.noteit.notifications

import android.content.Context
import android.util.Log
import androidx.work.*
import com.example.noteit.data.model.Task
import com.example.noteit.helpers.NotificationPreferences
import com.example.noteit.workers.NotificationWorker
import java.util.concurrent.TimeUnit

object TaskNotificationManager {

    fun updateNotifications(context: Context, tasks: List<Task>) {
        val workManager = WorkManager.getInstance(context)
        val minutesBefore = NotificationPreferences.getNotificationTime(context)
        val advanceMillis = minutesBefore * 60 * 1000L

        tasks.forEach { task ->
            val workName = "task_notification_${task.id}"

            if (task.hasNotification) {
                Log.d("NotificationWorker", "Scheduling notification for task id: ${task.id}")

                val notifyTime = task.dueAt - advanceMillis
                val delay = notifyTime - System.currentTimeMillis()
                if (delay > 0) {
                    val data = workDataOf(
                        "taskId" to task.id,
                        "title" to task.title,
                        "description" to task.description
                    )

                    val request = OneTimeWorkRequestBuilder<NotificationWorker>()
                        .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                        .setInputData(data)
                        .addTag("task_notification")
                        .build()

                    workManager.enqueueUniqueWork(
                        workName,
                        ExistingWorkPolicy.REPLACE,
                        request
                    )
                }
            } else {
                workManager.cancelUniqueWork(workName)
            }
        }
    }
    fun cancelNotificationForTask(context: Context, taskId: Int) {
        val workManager = WorkManager.getInstance(context)
        val workName = "task_notification_$taskId"

        workManager.cancelUniqueWork(workName)

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as android.app.NotificationManager
        notificationManager.cancel(taskId.toInt())

    }
}
