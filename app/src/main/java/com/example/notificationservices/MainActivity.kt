package com.example.notificationservices

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.alert

class MainActivity : AppCompatActivity() {

    private val permssoinCode: Int = 1
    private var mReceiver: BroadcastReceiver? = null

    var number : String? = null
    var message : String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        requestPermssion()
        if(intent!=null){
            number = intent.getStringExtra("number")
            message = intent.getStringExtra("message")
            number_txtview.text = number
            message_txtview.text = message
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {

        when (requestCode) {

            permssoinCode -> if (grantResults.size > 0) {

                var locationPermissions = grantResults[0] === PackageManager.PERMISSION_GRANTED

                if (!locationPermissions) {
                    alert("Please grant permissoins ") {
                        positiveButton("OK") { }

                    }.show()
                }
            }
        }
    }

    fun requestPermssion() {
        ActivityCompat.requestPermissions(this, arrayOf("android.permission.READ_SMS", "android.permission.RECEIVE_SMS", "android.permission.SEND_SMS"), permssoinCode);
    }

    override fun onResume() {
        super.onResume()
        val intentFilter = IntentFilter(
            "android.intent.action.SMSRECEBIDO")

        mReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                number = intent?.getStringExtra("number")
                message = intent?.getStringExtra("message")
                number_txtview.text = number
                message_txtview.text = message
            }
        }
        this.registerReceiver(mReceiver, intentFilter)
    }

    override fun onStop() {
        val intentFilter = IntentFilter(
            "android.intent.action.SMSRECEBIDO")
        this.registerReceiver(mReceiver, intentFilter)
        super.onStop()
    }
}
