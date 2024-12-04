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
            Log.d(TAG, "OkHttp: $message")
        }.apply {
            level = HttpLoggingInterceptor.Level.BODY
            redactHeader("Authorization")
            redactHeader("Cookie")
        }
    }

    private fun createNetworkInterceptor(): Interceptor {
        return Interceptor { chain ->
            val request = chain.request()
            Log.d(TAG, "Making request: ${request.method} ${request.url}")
            
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
            } catch (e: SocketTimeoutException) {
                Log.e(TAG, "Timeout error for ${request.url}", e)
                throw IOException("Network timeout. The server might be starting up, please try again in a minute.", e)
            } catch (e: UnknownHostException) {
                Log.e(TAG, "Unknown host error for ${request.url}", e)
                throw IOException("Unable to reach server. Please check your internet connection.", e)
            } catch (e: Exception) {
                Log.e(TAG, "Network error for ${request.url}", e)
                throw e
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
