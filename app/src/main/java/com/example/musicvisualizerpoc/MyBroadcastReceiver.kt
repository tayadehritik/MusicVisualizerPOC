package com.example.musicvisualizerpoc

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast

private const val TAG = "MyBroadcastReceiver"

class MyBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        if (intent.action.equals(Intent.ACTION_SCREEN_OFF)) {
            Log.d("hritik", "Screen went off")
            Toast.makeText(context, "screen OFF",Toast.LENGTH_LONG).show();
        } else if (intent.action.equals(Intent.ACTION_SCREEN_ON)) {
            Log.d("hritik", "Screen went on")
            Toast.makeText(context, "screen ON",Toast.LENGTH_LONG).show();
        }

    }
}
