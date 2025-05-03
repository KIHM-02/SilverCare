package com.hesdi.silvercare.entities

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build

class Canal_Notificacion
{
    companion object {
        const val CHANNEL_ID = "medicamento_channel"
    }

    fun createChannel(context: Context)
    {
        /*Deberia ser O, pero como la alarma requiere a S, para mentener
         * unicidad de versiones, usamos la version 12 de android.
         */

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
        {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Alarms_settled",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Canal de notificaciones para alarma de medicamentos"
            }

            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            notificationManager.createNotificationChannel(channel)
        }
    }
}