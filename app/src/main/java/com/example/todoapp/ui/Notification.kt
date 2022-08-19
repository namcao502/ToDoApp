package com.example.todoapp.ui

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.todoapp.R
import com.example.todoapp.ui.activities.MainActivity

const val notificationID = 502
const val channelID = "channel502"
const val titleExtra = "title502"
const val messageExtra = "message502"

class Notification: BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        val intent2 = Intent(context, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP

        val pendingIntent = PendingIntent.getActivity(context, notificationID, intent2, PendingIntent.FLAG_UPDATE_CURRENT)

        val notification = NotificationCompat.Builder(context, channelID)
            .setContentIntent(pendingIntent)
            .setSmallIcon(R.drawable.icons8_list_50)
            .setContentTitle(intent.getStringExtra(titleExtra))
            .setContentText(intent.getStringExtra(messageExtra))
            .setAutoCancel(true)
            .build()

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(notificationID, notification)
    }
}