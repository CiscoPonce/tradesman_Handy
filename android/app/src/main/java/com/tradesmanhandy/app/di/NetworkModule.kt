package com.tradesmanhandy.app.di

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.tradesmanhandy.app.data.api.TradesmanHandyApi
import com.tradesmanhandy.app.data.repository.BookingRepository
import com.tradesmanhandy.app.domain.repository.IBookingRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import android.util.Log
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.io.IOException
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okio.Buffer

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val TAG = "NetworkModule"
    private const val BASE_URL = "https://tradesman-handy.onrender.com/"
    private const val TIMEOUT_SECONDS = 120L  // Increased timeout for Render's free tier cold start

    @Provides
    @Singleton
    fun provideMoshi(): Moshi {
        return Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    private fun createLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor { message ->
            // Enhanced logging for request bodies
            if (message.startsWith("{") || message.startsWith("[")) {
                Log.d(TAG, "Request/Response Body: $message")
            } else {
                Log.d(TAG, "OkHttp: $message")
            }
        }.apply {
            level = HttpLoggingInterceptor.Level.BODY
            redactHeader("Authorization")
            redactHeader("Cookie")
        }
    }

    private fun createNetworkInterceptor(): Interceptor {
        return Interceptor { chain ->
            val original = chain.request()
            val request = original.newBuilder().apply {
                header("Content-Type", "application/json")
                method(original.method, original.body)
            }.build()

            // Log the complete request body
            request.body?.let { requestBody ->
                val buffer = Buffer()
                requestBody.writeTo(buffer)
                val bodyStr = buffer.readUtf8()
                Log.d(TAG, "Complete Request Body: $bodyStr")
            }

            try {
                val response = chain.proceed(request)
                Log.d(TAG, "Received response: ${response.code} for ${request.url}")
                
                if (!response.isSuccessful) {
                    val errorBody = response.peekBody(Long.MAX_VALUE).string()
                    Log.e(TAG, "Error response: ${response.code}")
                    Log.e(TAG, "Error body: $errorBody")
                    Log.e(TAG, "Request URL: ${request.url}")
                    Log.e(TAG, "Request method: ${request.method}")
                    Log.e(TAG, "Request headers: ${request.headers}")
                }
                response
            } catch (e: Exception) {
                when (e) {
                    is SocketTimeoutException -> {
                        Log.e(TAG, "Network timeout error", e)
                        throw IOException("Network timeout. Please check your connection and try again.")
                    }
                    is UnknownHostException -> {
                        Log.e(TAG, "Network host not found", e)
                        throw IOException("Network error. Please check your connection and try again.")
                    }
                    else -> {
                        Log.e(TAG, "Network error", e)
                        throw e
                    }
                }
            }
        }
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(createLoggingInterceptor())
            .addInterceptor(createNetworkInterceptor())
            .connectTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient, moshi: Moshi): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .also { Log.d(TAG, "Created Retrofit instance with base URL: $BASE_URL") }
    }

    @Provides
    @Singleton
    fun provideTradesmanHandyApi(retrofit: Retrofit): TradesmanHandyApi {
        return retrofit.create(TradesmanHandyApi::class.java)
            .also { Log.d(TAG, "Created TradesmanHandyApi instance") }
    }

    @Provides
    @Singleton
    fun provideBookingRepository(api: TradesmanHandyApi): IBookingRepository {
        return BookingRepository(api)
            .also { Log.d(TAG, "Created BookingRepository instance") }
    }
}
