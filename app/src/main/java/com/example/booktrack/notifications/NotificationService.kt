package com.example.booktrack.notifications


import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.example.booktrack.utils.NotificationHelper

class NotificationService : Service() {

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        // Vous pouvez configurer ici l'intervalle d'envoi des notifications.
        NotificationHelper.sendNotification(applicationContext, "C'est le moment de lire !")
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}
