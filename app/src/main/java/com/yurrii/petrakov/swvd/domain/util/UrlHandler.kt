package com.yurrii.petrakov.swvd.domain.util

import android.util.Log
import androidx.core.net.toUri
import java.net.HttpURLConnection
import java.net.URL
import kotlin.io.encoding.Base64

class UrlHandler {

    val allowList = listOf(
        "aHR0cHM6Ly9uZXdzLnljb21iaW5hdG9yLmNvbS8=",
        "aHR0cHM6Ly9leGFtcGxlLmNvbS8=",
    )

    fun decodeUrl(string: String): String {
        val decodedUrl = String(Base64.decode(string), Charsets.UTF_8)
        return decodedUrl
    }

    fun httpRedirection(url: String): String {
        return try {
            var currentUrl = url
            var redirect: String?

            do {
                val connection = (URL(currentUrl).openConnection() as HttpURLConnection).apply {
                    instanceFollowRedirects = false
                    requestMethod = "GET"
                    connectTimeout = 5000
                    readTimeout = 5000
                }

                connection.connect()

                redirect = when (connection.responseCode) {
                    HttpURLConnection.HTTP_MOVED_PERM,
                    HttpURLConnection.HTTP_MOVED_TEMP,
                    HttpURLConnection.HTTP_SEE_OTHER,
                    307, 308 -> connection.getHeaderField("Location")

                    else -> null
                }

                connection.disconnect()

                if (redirect != null) {
                    currentUrl = if (redirect.startsWith("http")) redirect
                    else URL(URL(currentUrl), redirect).toString()
                }

            } while (redirect != null)

            currentUrl
        } catch (e: Exception) {
            url
        }
    }

    fun validateDeepLink(deepLink: String): Boolean {
        return try {
            val uri = deepLink.toUri()

            val isStructureValid =
                uri.scheme == "myapp" &&
                        uri.host == "game" &&
                        !uri.getQueryParameter("url").isNullOrBlank() &&
                        !uri.getQueryParameter("title").isNullOrBlank()

            if (!isStructureValid) return false

            val encodedUrl = uri.getQueryParameter("url") ?: return false

            val decodedUrl = try {
                decodeUrl(encodedUrl)
            } catch (e: Exception) {
                return false
            }

            val decodedUri = decodedUrl.toUri()
            val schemeOk = decodedUri.scheme == "http" || decodedUri.scheme == "https"
            if (!schemeOk) return false

            return ifUrlInAllowList(decodedUrl)

        } catch (e: Exception) {
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