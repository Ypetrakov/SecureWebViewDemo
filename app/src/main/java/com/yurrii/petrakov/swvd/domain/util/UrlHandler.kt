package com.yurrii.petrakov.swvd.domain.util

import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import kotlin.io.encoding.Base64

class UrlHandler {

    val allowList = listOf(
        "aHR0cHM6Ly9uZXdzLnljb21iaW5hdG9yLmNvbS8=",
        "aHR0cHM6Ly9leGFtcGxlLmNvbS8=",
        "aHR0cHM6Ly93d3cucmVmcmVzaHlvdXJjYWNoZS5jb20vZW4vY2FjaGUtdGVzdA=="
    )

    fun decodeUrl(string: String): String {
        val decodedUrl = String(Base64.decode(string), Charsets.UTF_8)
        return decodedUrl
    }

    fun validateDeepLink(data: Uri?): Boolean {
        return try {
            Log.d("DeepLink", "validateDeepLink called with: $data")

            if (data == null) {
                Log.d("DeepLink", "FAILED: data is null")
                return false
            }

            val allowedSchemes = setOf("myapp", "fdsqwoxss")

            val scheme = data.scheme
            if (scheme == null) {
                Log.d("DeepLink", "FAILED: scheme is null")
                return false
            }

            Log.d("DeepLink", "scheme = $scheme")

            if (scheme !in allowedSchemes) {
                Log.d("DeepLink", "FAILED: scheme not allowed -> $scheme")
                return false
            }

            val host = data.host
            Log.d("DeepLink", "host = $host")

            if (host != "game" && host != "menu") {
                Log.d("DeepLink", "FAILED: invalid host -> $host")
                return false
            }

            val encodedUrl = data.getQueryParameter("url")
            val title = data.getQueryParameter("title")

            Log.d("DeepLink", "encodedUrl = $encodedUrl")
            Log.d("DeepLink", "title = $title")

            if (encodedUrl.isNullOrBlank()) {
                Log.d("DeepLink", "FAILED: missing url or title")

                return true
            }

            val decodedUrl = try {
                val result = decodeUrl(encodedUrl)
                Log.d("DeepLink", "decodedUrl = $result")
                result
            } catch (e: Exception) {
                Log.d("DeepLink", "FAILED: decodeUrl exception = ${e.message}")
                return false
            }

            val decodedUri = decodedUrl.toUri()
            val decodedScheme = decodedUri.scheme

            Log.d("DeepLink", "decodedScheme = $decodedScheme")

            if (decodedScheme == null) {
                Log.d("DeepLink", "FAILED: decoded scheme is null")
                return false
            }

            val schemeOk = decodedScheme == "http" || decodedScheme == "https"

            Log.d("DeepLink", "schemeOk = $schemeOk")

            if (!schemeOk) {
                Log.d("DeepLink", "FAILED: decoded scheme not http/https")
                return false
            }

            Log.d("DeepLink", "SUCCESS: deep link is valid")
            return true

        } catch (e: Exception) {
            Log.e("DeepLink", "CRASH in validateDeepLink", e)
            false
        }
    }
    fun ifUrlInAllowList(url: String, isDecoded: Boolean = false): Boolean {
        Log.d("TEST", url)
        return allowList.map { decodeUrl(it) }.any { allowed ->
            try {
                val allowedUri = allowed.toUri()
                val inputUri = decodeUrl(url).toUri()

                allowedUri.host == inputUri.host
                        && inputUri.toString().startsWith(allowed)
            } catch (e: Exception) {
                false
            }
        }
    }
}