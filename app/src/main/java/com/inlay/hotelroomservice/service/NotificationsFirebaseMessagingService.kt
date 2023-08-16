package com.inlay.hotelroomservice.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.inlay.hotelroomservice.R
import com.inlay.hotelroomservice.domain.usecase.datastore.notifications.GetNotificationsState
import com.inlay.hotelroomservice.presentation.activities.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

private const val CHANNEL_ID = "default_notification_channel_id"

class NotificationsFirebaseMessagingService : FirebaseMessagingService() {
    private val getNotificationsState: GetNotificationsState by inject()
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        CoroutineScope(Dispatchers.IO).launch {
            getNotificationsState().collect {
                if (!it) {
                    if (message.notification != null) generateNotification(
                        message.notification!!.title!!,
                        message.notification!!.body!!
                    )
                }
            }
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

    //    @SuppressLint("RemoteViewLayout")
//    private fun getRemoteViews(title: String, message: String) {
//        val remoteViews =
//            RemoteViews("com.inlay.hotelroomservice.service", R.layout.push_notification)
//
//        remoteViews.setTextViewText(R.id.tv_notification_title, title)
//        remoteViews.setTextViewText(R.id.tv_notification_message, message)
//        remoteViews.setImageViewResource(R.id.img_notification_icon, R.mipmap.ic_launcher_round)
//    }

    private fun generateNotification(title: String, message: String) {
        val intent = Intent(this, MainActivity::class.java)

        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setAutoCancel(true)
            .setOnlyAlertOnce(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)

        createNotificationsChanel(notificationManager)

        notificationManager.notify(0, builder.build())
    }

    private fun createNotificationsChanel(notificationManager: NotificationManager) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelName = getString(R.string.default_notification_channel_id)
            val descriptionText = getString(R.string.notifications_channel_description)

            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, channelName, importance).apply {
                description = descriptionText
            }

            notificationManager.createNotificationChannel(channel)
        }
    }
}