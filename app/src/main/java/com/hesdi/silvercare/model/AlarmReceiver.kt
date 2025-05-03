package com.hesdi.silvercare.model

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import com.hesdi.silvercare.entities.Canal_Notificacion.Companion.CHANNEL_ID
import com.hesdi.silvercare.R
import com.hesdi.silvercare.views.Recordatorios

class AlarmReceiver : BroadcastReceiver()
{
    companion object{
        const val NOTIFICATION_ID = 100
    }

    override fun onReceive(context: Context, intent: Intent?) {
        Log.d("AlarmReceiver", "Alarma recibida")
        createSimpleNotification(context)
    }

    fun createSimpleNotification(context: Context)
    {
        val intent = Intent(context, Recordatorios::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val flag = PendingIntent.FLAG_IMMUTABLE

        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            flag
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.applogo4)
            .setContentTitle("SilverCare")
            .setContentText("Esto es la notificacion prueba")
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("Recuerde tomar su medicamento correspondiente")
            )
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(NOTIFICATION_ID, notification)
    }
}