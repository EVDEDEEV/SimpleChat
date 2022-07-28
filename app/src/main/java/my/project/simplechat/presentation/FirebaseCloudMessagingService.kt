package my.project.simplechat.presentation

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import my.project.simplechat.R

const val CHANNEL_ID = "notification_channel"
const val CHANNEL_NAME = "my.project.simplechat"


@SuppressLint("MissingFirebaseInstanceTokenRefresh")
class FirebaseCloudMessagingService : FirebaseMessagingService() {

    private val vibrationPattern = longArrayOf(1000, 1000, 1000, 1000)

    //generate the notification
    //attach the notification created with the custom layout
    //show the notification

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        if (remoteMessage.notification != null) {
            generateNotification(remoteMessage.notification!!.title!!, remoteMessage.notification!!.body!!)
        }
    }

    private fun getRemoteView(title: String, message: String): RemoteViews {
        val remoteView = RemoteViews(CHANNEL_NAME, R.layout.notification)

        remoteView.setTextViewText(R.id.notific_title, title)
        remoteView.setTextViewText(R.id.notific_message, message)
        remoteView.setImageViewResource(R.id.notific_logo, R.drawable.open_src)

        return remoteView
    }

    private fun generateNotification(title: String, message: String) {

        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)

        //channel id, channel name
        var builder: NotificationCompat.Builder =
            NotificationCompat.Builder(applicationContext, CHANNEL_ID)
                .setSmallIcon(R.drawable.open_src)
                .setAutoCancel(true)
                .setVibrate(vibrationPattern)
                .setOnlyAlertOnce(true)
                .setContentIntent(pendingIntent)
        builder = builder.setContent(getRemoteView(title, message))

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel =
                NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(notificationChannel)
        }

        notificationManager.notify(0, builder.build())
    }
}