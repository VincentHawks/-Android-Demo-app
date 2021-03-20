package com.swtecnn.demomode

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Binder
import android.os.IBinder
import android.os.PowerManager

/**
 * The wrapper foreground service that hosts IdleModeReceiver
 */
class IdleModeReceiverHostService : Service() {

    val receiver = IdleModeReceiver()

    val binder = ReceiverHostBinder()

    inner class ReceiverHostBinder : Binder() {
        fun getService() = this@IdleModeReceiverHostService
    }

    companion object {
        const val CHANNEL_ID = "DemoMode_IdleModeReceiverHostServiceHostNotificationChannel"
    }

    override fun onBind(intent: Intent?): IBinder? {
        return binder
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        // Register the receiver
        val filter = IntentFilter()
        filter.addAction(Intent.ACTION_SCREEN_OFF)
        registerReceiver(receiver, filter)

        // Create host notification
        val notification = Notification.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("This device is in demo mode")
            .setContentText("Reboot the device or force stop the \"demo mode\" app to exit demo mode")
            .setOnlyAlertOnce(true)
            .setOngoing(true)
            .build()

        val channel = NotificationChannel(
            CHANNEL_ID,
            "Demo mode: Notification channel to host the foreground service that runs the receiver for idle mode state events",
            NotificationManager.IMPORTANCE_LOW
        )
        channel.setSound(null, null)

        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)
        startForeground(100, notification)

        return START_STICKY
    }

    override fun onDestroy() {
        unregisterReceiver(receiver)
        super.onDestroy()
    }

}