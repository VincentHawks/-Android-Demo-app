package com.swtecnn.demomode

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.PowerManager


/**
 * The background receiver that wakes up the video activity when one of the following happens:
 * 1. Battery is plugged in
 * 2. Screen turns off
 *
 * MUST BE REGISTERED WITH THOSE INTENT FILTERS
 */
class IdleModeReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        when(intent?.action) {
            Intent.ACTION_SCREEN_OFF -> {
                val powerManager =
                    (context?.getSystemService(Context.POWER_SERVICE) as PowerManager)
                val wakeLock = powerManager.newWakeLock(
                    (
                    PowerManager.SCREEN_BRIGHT_WAKE_LOCK
                    or PowerManager.FULL_WAKE_LOCK
                    or PowerManager.ACQUIRE_CAUSES_WAKEUP
                    ),
                "DemoMode:IdleModeReceiver"
                )
                wakeLock.acquire(1000)

                val mainActivityIntent = Intent(context, MainActivity::class.java)
                mainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                val pendingIntent = PendingIntent.getActivity(context, 0, mainActivityIntent, 0)
                pendingIntent.send()
            }
        }
    }

}