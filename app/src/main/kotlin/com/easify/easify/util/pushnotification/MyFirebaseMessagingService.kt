package com.easify.easify.util.pushnotification

import android.content.Intent
import android.os.Handler
import android.os.Looper
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

/**
 * @author: deniz.demirci
 * @date: 20.12.2020
 */

class MyFirebaseMessagingService : FirebaseMessagingService() {
  private var broadcaster: LocalBroadcastManager? = null
  private val processLater = false

  override fun onCreate() {
    broadcaster = LocalBroadcastManager.getInstance(this)
  }

  override fun onNewToken(token: String) {
    getSharedPreferences("_", MODE_PRIVATE).edit().putString("fcm_token", token).apply()
  }

  override fun onMessageReceived(remoteMessage: RemoteMessage) {
    super.onMessageReceived(remoteMessage)



    if (/* Check if data needs to be processed by long running job */ processLater) {
      //scheduleJob()
    } else {
      // Handle message within 10 seconds
      handleNow(remoteMessage)
    }
  }

  private fun handleNow(remoteMessage: RemoteMessage) {
    val handler = Handler(Looper.getMainLooper())

    handler.post {
      //Toast.makeText(baseContext, "aaaaaa", Toast.LENGTH_LONG).show()

      remoteMessage.notification?.let {
        val intent = Intent("MyData")
        intent.putExtra("message", remoteMessage.data["text"])
        broadcaster?.sendBroadcast(intent)
      }
    }
  }
}
