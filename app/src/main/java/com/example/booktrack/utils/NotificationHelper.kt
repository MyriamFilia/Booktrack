package com.example.booktrack.utils

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.booktrack.R

object NotificationHelper {

    private const val CHANNEL_ID = "booktrack_notifications"
    private const val CHANNEL_NAME = "BookTrack Notifications"
    private const val CHANNEL_DESCRIPTION = "Channel for BookTrack reminders"
    private const val NOTIFICATION_ID = 1 // Utilisez un ID unique pour la notification

    // Cette fonction crée un canal de notification, nécessaire sur Android 8.0 (API 26) et plus
    fun createNotificationChannel(context: Context) {
        // Créer le canal de notification uniquement si l'API est >= 26
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = CHANNEL_DESCRIPTION
            }

            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    // Cette fonction envoie une notification
    fun sendNotification(context: Context, message: String) {
        // Créer la notification avec NotificationCompat.Builder pour la compatibilité
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification) // Icône de notification
            .setContentTitle("Rappel BookTrack") // Titre de la notification
            .setContentText(message) // Le message de la notification
            .setPriority(NotificationCompat.PRIORITY_DEFAULT) // Priorité de la notification
            .setAutoCancel(true) // Ferme la notification lorsque l'utilisateur clique dessus
            .build()

        // Obtenez le NotificationManager pour envoyer la notification
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTIFICATION_ID, notification)
    }
}
