package com.example.musicvisualizerpoc

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.startActivity

private const val TAG = "MyBroadcastReceiver"

private const val CHANNEL_ID = "exampleServiceChannel"

class MyBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        if (intent.action.equals(Intent.ACTION_SCREEN_OFF)) {
            Log.d("hritik", "Screen went off")
            Toast.makeText(context, "screen OFF",Toast.LENGTH_LONG).show();

            context.showNotificationWithFullScreenIntent(true)

        } else if (intent.action.equals(Intent.ACTION_SCREEN_ON)) {
            Log.d("hritik", "Screen went on")
            Toast.makeText(context, "screen ON",Toast.LENGTH_LONG).show();
        }


    }
}

