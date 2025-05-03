package com.hesdi.silvercare.entities

import android.Manifest.permission.POST_NOTIFICATIONS
import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.icu.util.Calendar
import android.os.Build
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.hesdi.silvercare.model.AlarmReceiver
import com.hesdi.silvercare.model.AlarmReceiver.Companion.NOTIFICATION_ID

class Medicamento(
    private var nombre: String = "",
    private var imgUrl: String = "",
    private var periodo: Int = 0,
    private var intervalo: Int = 0,
    private var hora: String = "",
    private var userId: String = ""
) {

    private val db = Firebase.firestore

    fun insertData(): Boolean
    {
        return try
        {
            val medicamento = db.collection("Medicamento")

            val data = hashMapOf(
                "nombre" to this.nombre,
                "imgUrl" to this.imgUrl,
                "periodo" to this.periodo,
                "intervalo" to this.intervalo,
                "hora" to hora,
                "userId" to userId
            )

            medicamento.add(data) // Esto genera un ID único automáticamente
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun scheduleNotification(context: Context)
    {
        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            NOTIFICATION_ID,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            Calendar.getInstance().timeInMillis + 15000,
            pendingIntent
        )
    }

    fun verifyAlarmManager(context: Context): Boolean
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val alarmManager = ContextCompat.getSystemService(context, AlarmManager::class.java)
            if (alarmManager?.canScheduleExactAlarms() == false) {
                Intent().also { intent ->
                    intent.action = Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM
                    context.startActivity(intent)
                }

                return false
            }
        }

        return true
    }

    fun verifyNotification(context: Context): Boolean
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(context, POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED
            ) {

                ActivityCompat.requestPermissions(
                    context as Activity,
                    arrayOf(POST_NOTIFICATIONS),
                    1
                )

                return false
            }
        }
        return true
    }


}