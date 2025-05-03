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
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val startMillis = parseHoraToMillis(hora)

        for (i in 0..periodo step intervalo)
        { // periodo = días del tratamiento
            val triggerTime = startMillis + (i * 24 * 60 * 60 * 1000L)

            val intent = Intent(context, AlarmReceiver::class.java).apply {
                putExtra("nombre", nombre)
            }

            val pendingIntent = PendingIntent.getBroadcast(
                context,
                generateRequestCode(i), // Único para cada día
                intent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )

            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                triggerTime,
                pendingIntent
            )
        }
    }

    fun parseHoraToMillis(hora: String): Long
    {
        val parts = hora.split(":")
        val horaInt = parts[0].toInt()
        val minutoInt = parts[1].toInt()

        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, horaInt)
            set(Calendar.MINUTE, minutoInt)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)

            if (timeInMillis < System.currentTimeMillis()) {
                add(Calendar.DAY_OF_MONTH, 1) // Si es para hoy y ya pasó, agendar mañana
            }
        }

        return calendar.timeInMillis
    }

    fun generateRequestCode(dia: Int): Int {
        return (nombre + dia.toString()).hashCode() // Puedes incluir `userId` también si hace falta
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