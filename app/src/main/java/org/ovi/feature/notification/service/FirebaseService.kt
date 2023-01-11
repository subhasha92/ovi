package org.ovi.feature.notification.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.ovi.R
import org.ovi.data.local.DataUtil
import org.ovi.data.pref.OVIPreferences
import org.ovi.feature.splash.SplashScreenActivity


class FirebaseService : FirebaseMessagingService() {
    /**
     * There are two scenarios when onNewToken is called:
     * 1) When a new token is generated on initial app startup
     * 2) Whenever an existing token is changed
     * Under #2, there are three scenarios when the existing token is changed:
     * A) App is restored to a new device
     * B) User uninstalls/reinstalls the app
     * C) User clears app data
     */
    override fun onNewToken(token: String) {
//        Log.d(TAG, "Refreshed token: $token")
        val pref = OVIPreferences()
        pref.fireToken = token
    }


    override fun onMessageReceived(remoteMessage: RemoteMessage) {
//        Log.d(TAG, "onMessageReceived: Started : ${remoteMessage.toString()}")
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
//        Log.d(TAG, "From: ${remoteMessage.from}")
//        Log.e(TAG, "Message data payload: ${remoteMessage.data}")
//        Log.e(TAG, "Message notification payload: ${remoteMessage.notification.toJsonString()}")
        // Check if message contains a data payload.
        if (remoteMessage.notification!=null) {
            sendNotification(remoteMessage.notification?.title, remoteMessage.notification?.body)
        }

    }

    private fun sendNotification(title: String?, body: String?) {
//        Log.e("SendNoti", "$title : $body")
//        Log.d(TAG, "sendNotification: Started")
        val channelId: String = getString(R.string.channel_name)
//        Log.d(TAG, "sendNotification: $channelId")
        createNotificationChannel(channelId)

        val intent = Intent(this, SplashScreenActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent =
            PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val largeIcon = BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher_round)

        val builder = channelId.let {
            NotificationCompat.Builder(this, it)
                .setSmallIcon(R.drawable.ic_notification_icon)
                .setLargeIcon(largeIcon)
//                .setColor(getColor(R.color.white_1000))
                .setContentTitle(title)
                .setContentText(body)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        }

        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(DataUtil.NOTIFICATION_ID, builder.build())
    }

    private fun createNotificationChannel(channelId: String?) {

        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel =
                NotificationChannel(channelId, name, importance).apply {
                    description = descriptionText
                }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}