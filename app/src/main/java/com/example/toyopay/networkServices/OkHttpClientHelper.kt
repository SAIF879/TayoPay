package com.example.toyopay.networkServices


import android.util.Log
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.example.toyopay.BuildConfig
import com.example.toyopay.TayoPayApp
import com.example.toyopay.util.sharedPref.SharedPref
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.OkHttpClient.Builder
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import java.io.File
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class OkHttpClientHelper(private val sharedPref: SharedPref) {


    companion object {
        const val CONNECT_TIMEOUT = "CONNECT_TIMEOUT"
        const val READ_TIMEOUT = "READ_TIMEOUT"
        const val WRITE_TIMEOUT = "WRITE_TIMEOUT"
        const val CACHED_TIME_SECONDS = "CACHED_TIME_SECONDS"
    }

    fun getOkHttpClient(): OkHttpClient {
        val httpCacheDirectory = File(TayoPayApp.instance?.cacheDir, "http-cache")
        val cacheSize = 10 * 1024 * 1024 // 10 MB
        val cache = Cache(httpCacheDirectory, cacheSize.toLong())
        val client: Builder = Builder()
            .addInterceptor(getHttpLoggingInterceptor())
            .addInterceptor(getRequestInterceptor())
//        if (BuildConfig.DEBUG) {
        getChuckerInterceptor()?.let { client.addInterceptor(it) }
//        }
        client.cache(cache)
        return client.build()
    }

    private fun getHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val httpLoggingInterceptor =
            HttpLoggingInterceptor { message ->
                Log.d(
                    "message",
                    "getHttpLoggingInterceptor:${message} "
                )
            }
//        if (BuildConfig.DEBUG)
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
//        else
//            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.NONE
        return httpLoggingInterceptor
    }

    private fun getRequestInterceptor(): Interceptor {
        return Interceptor { chain: Interceptor.Chain ->
            val request = chain.request()
            var connectTimeout = 30 * 1000
            var readTimeout = 30 * 1000
            var writeTimeout = 2 * 60 * 1000
            val connectNew = request.header(CONNECT_TIMEOUT)
            val readNew = request.header(READ_TIMEOUT)
            val writeNew = request.header(WRITE_TIMEOUT)
            val cachedTimeInSecs = request.header(CACHED_TIME_SECONDS)
            if (!connectNew.isNullOrEmpty()) {
                connectTimeout = connectNew.toInt()
            }
            if (!readNew.isNullOrEmpty()) {
                readTimeout = readNew.toInt()
            }
            if (!writeNew.isNullOrEmpty()) {
                Log.e("ERROR_____>", "getRequestInterceptor: $writeNew")
                writeTimeout = writeNew.toInt()
            }
            try {
                var originalResponse = chain
                    .withConnectTimeout(connectTimeout, TimeUnit.MILLISECONDS)
                    .withReadTimeout(readTimeout, TimeUnit.MILLISECONDS)
                    .withWriteTimeout(writeTimeout, TimeUnit.MILLISECONDS)
                    .proceed(getHeaders(request))
                if (!cachedTimeInSecs.isNullOrEmpty()) {
                    val maxAge = cachedTimeInSecs.toInt()
                    originalResponse = originalResponse
                        .newBuilder()
                        .addHeader("Cache-Control", "public, max-age=$maxAge")
                        .build()
                }
                originalResponse
            } catch (e: SocketTimeoutException) {
                Response.Builder()
                    .request(request)
                    .code(999)
                    .protocol(Protocol.HTTP_1_1)
                    .body(
                        "{$e}".toResponseBody(
                            "".toRequestBody(("application/json").toMediaTypeOrNull()).contentType()
                        )
                    )
                    .message("socketTimeOut $e")
                    .build()
            } catch (e: UnknownHostException) {
                Response.Builder()
                    .request(request)
                    .code(999)
                    .protocol(Protocol.HTTP_1_1)
                    .body(
                        "{$e}".toResponseBody(
                            "".toRequestBody(("application/json").toMediaTypeOrNull()).contentType()
                        )
                    )
                    .message("unKnownHostException $e")
                    .build()
            }

        }
    }


    private fun getChuckerInterceptor(): ChuckerInterceptor? {
        Log.d("CHUCKER_CHECK", "getChuckerInterceptor: ")
        val context = TayoPayApp.instance
        return context?.let {
            ChuckerInterceptor.Builder(it)
                .collector(ChuckerCollector(context))
                .alwaysReadResponseBody(true)
                .build()
        }
    }


    private fun getHeaders(request: Request): Request {
        val versionCode = BuildConfig.VERSION_CODE.toString()
        val req = request.newBuilder()
            .header("APP-VERSION-CODE", versionCode)
            .header("Content-Type", "application/json")
            .header("Accept", "application/json")
        sharedPref.authToken?.let {
            req.header("Authorization", "Bearer ${sharedPref.authToken?:""}")
        }
        return req.build()
    }


}
