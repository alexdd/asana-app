package com.asana.timer

import android.content.Context
import android.Manifest
import android.location.Location
import android.util.Log
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.suspendCancellableCoroutine
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

private const val TAG = "LocationSyncRunner"

/**
 * Runs location sync once: gets last location, POSTs to configured endpoint, updates [LocationSyncStatus].
 * Can be called from Worker or from UI (e.g. "Sync testen" button).
 */
suspend fun runLocationSync(context: Context): SyncResult {
    val hasFine = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == android.content.pm.PackageManager.PERMISSION_GRANTED
    val hasCoarse = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == android.content.pm.PackageManager.PERMISSION_GRANTED

    if (!hasFine && !hasCoarse) {
        val msg = "Keine Standort-Berechtigung"
        Log.w(TAG, msg)
        LocationSyncStatus.markFailure(msg)
        return SyncResult.Failure(msg)
    }

    val location = getLastLocation(context)
    if (location == null) {
        val msg = "Keine Standortdaten (GPS einmal aktivieren)"
        Log.w(TAG, msg)
        LocationSyncStatus.markFailure(msg)
        return SyncResult.Failure(msg)
    }

    val host = BuildConfig.LOCATION_SYNC_HOST.trim().trimEnd('/')
    val path = BuildConfig.LOCATION_SYNC_PATH.trim()
    val secret = BuildConfig.LOCATION_SYNC_SECRET
    val urlString = "$host$path"
    val payload = buildJsonPayload(location)

    return try {
        Log.d(TAG, "POST $urlString")
        Log.i(TAG, "Sent JSON: $payload")
        val url = URL(urlString)
        val connection = (url.openConnection() as HttpURLConnection).apply {
            requestMethod = "POST"
            connectTimeout = 15_000
            readTimeout = 15_000
            doInput = true
            doOutput = true
            setRequestProperty("Content-Type", "application/json; charset=utf-8")
            setRequestProperty("Accept", "application/json")
            setRequestProperty("X-Internal-Secret", secret)
        }

        connection.outputStream.use { os ->
            os.write(payload.toByteArray(Charsets.UTF_8))
            os.flush()
        }

        val responseCode = connection.responseCode
        val errorBody = if (responseCode !in 200..299) {
            try {
                BufferedReader(InputStreamReader(connection.errorStream ?: connection.inputStream, Charsets.UTF_8)).readText()
            } catch (_: Exception) {
                null
            }
        } else null
        connection.disconnect()

        if (responseCode in 200..299) {
            LocationSyncStatus.markSuccess()
            Log.d(TAG, "Sync OK")
            SyncResult.Success
        } else {
            val msg = "Server: $responseCode${if (errorBody?.isNotBlank() == true) " – $errorBody" else ""}"
            Log.w(TAG, msg)
            LocationSyncStatus.markFailure(msg)
            SyncResult.Failure(msg)
        }
    } catch (e: Exception) {
        val msg = e.message ?: e.javaClass.simpleName
        Log.e(TAG, "Sync failed: $msg", e)
        LocationSyncStatus.markFailure(msg)
        SyncResult.Failure(msg)
    }
}

private suspend fun getLastLocation(context: Context): Location? {
    val client = LocationServices.getFusedLocationProviderClient(context)
    return try {
        suspendCancellableCoroutine { cont ->
            val task = client.lastLocation
            task.addOnSuccessListener { if (cont.isActive) cont.resume(it) }
            task.addOnFailureListener { e -> if (cont.isActive) cont.resumeWithException(e) }
        }
    } catch (_: Exception) {
        null
    }
}

/** Exakt die vom Endpoint erwartete Struktur: address (string), lat, lng – kein Geocoding. */
private fun buildJsonPayload(location: Location): String {
    val lat = location.latitude
    val lng = location.longitude
    val address = "" // leer lassen, Server soll nicht geocoden
    return """{"address":"$address","lat":$lat,"lng":$lng}"""
}

sealed class SyncResult {
    object Success : SyncResult()
    data class Failure(val message: String) : SyncResult()
}
