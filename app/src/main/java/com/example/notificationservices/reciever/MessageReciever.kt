package com.example.notificationservices.reciever

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.telephony.SmsMessage
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import com.example.notificationservices.MainActivity
import com.example.notificationservices.R
import org.jetbrains.anko.longToast

class MessageReciever : BroadcastReceiver() {
    lateinit var notificationChannel: NotificationChannel
    lateinit var notificationManager: NotificationManager
    lateinit var builder: NotificationCompat.Builder
    private val channelId = "com.example.notifications"

    override fun onReceive(context: Context?, intent: Intent?) {
        val bundle = intent!!.extras

        try {
            if (bundle != null) {

                val objectArray = bundle.get("pdus") as Array<Any>

                for (i in 0 until objectArray.size) {

                    val smsMessage = SmsMessage.createFromPdu(objectArray[i] as ByteArray) as SmsMessage
                    val senderPhoneNumber = smsMessage.displayOriginatingAddress
                    val message = smsMessage.displayMessageBody

                    if (context != null) {
                        Toast.makeText(context,"Number : "+senderPhoneNumber + " " + "Message : "+ message,Toast.LENGTH_SHORT).show()
                    }

                    val i2 = Intent("android.intent.action.SMSRECEBIDO")
                        .putExtra("number", senderPhoneNumber)
                        .putExtra("message", message)
                         context?.sendBroadcast(i2)

                    if (context!=null){sendNotification(context,senderPhoneNumber,message)}

                }
            }
        } catch (e: Exception) {
            Log.e("MyBroadcastReceiver", e.toString())
        }
    }

    private fun sendNotification(context: Context, senderPhoneNumber: String?, message: String?) {
       //Notification Manager
        notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel =
                NotificationChannel(channelId, message, NotificationManager.IMPORTANCE_HIGH)
            notificationChannel.enableVibration(true)
            notificationManager.createNotificationChannel(notificationChannel)
        }


            val i = Intent(context, MainActivity::class.java)
            val pendingIntent =
                PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_UPDATE_CURRENT)
            builder = NotificationCompat.Builder(context, channelId)
                .setStyle(NotificationCompat.BigTextStyle().bigText(message))
                .setContentTitle(senderPhoneNumber)
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
            notificationManager.notify(1234, builder.build())


    }


}