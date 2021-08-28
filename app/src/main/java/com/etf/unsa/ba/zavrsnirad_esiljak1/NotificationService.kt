package com.etf.unsa.ba.zavrsnirad_esiljak1

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import androidx.core.app.NotificationManagerCompat
import com.ba.zavrsnirad_esiljak1.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class NotificationService: FirebaseMessagingService() {
    override fun onMessageReceived(p0: RemoteMessage) {
        val title = p0.notification!!.title
        val text = p0.notification!!.title
        val CHANNEL_ID = "HEADS_UP_NOTIFICATION"

        val channel = NotificationChannel(CHANNEL_ID, "Heads up notification", NotificationManager.IMPORTANCE_HIGH)
        getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
        val builder = Notification.Builder(this, CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(text)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setAutoCancel(true)
        NotificationManagerCompat.from(this).notify(1, builder.build())

        super.onMessageReceived(p0)
    }
}