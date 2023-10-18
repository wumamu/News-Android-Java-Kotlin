package com.recoveryrecord.surveyandroid.example.service

import android.content.ComponentName
import android.content.Context
import android.content.ServiceConnection
import android.os.IBinder
import android.provider.Settings
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification


class ModifiedNotificationListenerService : NotificationListenerService() {
    private var isServiceRunning = false // Track the service state
    override fun onCreate() {
        super.onCreate()
        // Check if the service is running when it's created
        isServiceRunning = isServiceRunning(this, ModifiedNotificationListenerService::class.java)
    }

    override fun onListenerConnected() {
        super.onListenerConnected()
        // Update the service state when it's connected
        isServiceRunning = true
    }

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        // Check if the service is running
        if (!isServiceRunning) {
            // Service is not running, show a notification or guide to restart it
            showRestartServiceNotification()
        }
        // Process the notification as usual
        // ...
    }

    // Other methods for processing notifications
    // Method to check if the service is running
    private fun isServiceRunning(context: Context, serviceClass: Class<*>): Boolean {
        val componentName = ComponentName(context, serviceClass)
        val enabledListeners = Settings.Secure.getString(
            context.contentResolver, "enabled_notification_listeners"
        )
        return enabledListeners != null && enabledListeners.contains(componentName.flattenToString())
    }

    // Method to show a notification or guide to restart the service
    private fun showRestartServiceNotification() {
        // Display a notification or guide to instruct the user to manually restart the service
        // You can use NotificationCompat to create a notification or display a dialog
        // ...
    }

    // Service Connection (for manual restart)
    private val serviceConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            // Service reconnected
            isServiceRunning = true
        }

        override fun onServiceDisconnected(name: ComponentName) {
            // Service disconnected
            isServiceRunning = false
        }
    }
}
