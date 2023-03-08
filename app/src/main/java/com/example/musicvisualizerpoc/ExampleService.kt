package com.example.musicvisualizerpoc

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat

class ExampleService: Service() {
    public val CHANNEL_ID = "exampleServiceChannel"
    val br: BroadcastReceiver = MyBroadcastReceiver()

    override fun onCreate() {
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this,
        0,notificationIntent,PendingIntent.FLAG_IMMUTABLE)

        val notification:Notification = NotificationCompat.Builder(this,CHANNEL_ID)
            .setContentTitle("Example Service")
            .setContentText("Example Test")
            .setSmallIcon(R.drawable.ic_android_black_24dp)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .build()


        val screenStateFilter = IntentFilter()
        screenStateFilter.addAction(Intent.ACTION_SCREEN_ON)
        screenStateFilter.addAction(Intent.ACTION_SCREEN_OFF)
        val listenToBroadcastsFromOtherApps = false
        val receiverFlags = if (listenToBroadcastsFromOtherApps) {
            ContextCompat.RECEIVER_EXPORTED
        } else {
            ContextCompat.RECEIVER_NOT_EXPORTED
        }
        ContextCompat.registerReceiver(this, br, screenStateFilter,receiverFlags)

        startForeground(1, notification)
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        unregisterReceiver(br)
        super.onDestroy()
    }


    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}