package io.github.paletteLens.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * This Broadcast receiver listens to Connectivity changes in the device, and is used to update
 */
class ConnectivityBroadcastReceiver(private val connectivityState: MutableStateFlow<Boolean?>) : BroadcastReceiver() {
    override fun onReceive(
        context: Context?,
        intent: Intent?,
    ) {
        Log.i("ConnectivityBroadcastReceiver", "Connectivity changed")
        val connectivityManager =
            context!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork
        if (activeNetwork == null) {
            Log.i("ConnectivityBroadcastReceiver", "No active network")
            connectivityState.value = false
            return
        }
        val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork)
        if (networkCapabilities == null) {
            Log.i("ConnectivityBroadcastReceiver", "No network capabilities")
            connectivityState.value = false
            return
        }
        if (connectivityState.value == null) {
            connectivityState.value = networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
            return
        } else {
            connectivityState.value = !networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
        }
        Log.i("ConnectivityBroadcastReceiver", "Connectivity state: ${connectivityState.value}")
        return
    }
}
