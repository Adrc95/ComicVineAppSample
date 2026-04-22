package com.adrc95.comicvineappsample.di

import android.content.Context
import com.adrc95.comicvineappsample.BuildConfig
import com.adrc95.comicvineappsample.data.server.service.CharacterService
import com.adrc95.comicvineappsample.data.server.ComicVineAuthInterceptor
import com.adrc95.comicvineappsample.di.qualifier.ApiKey
import com.adrc95.comicvineappsample.di.qualifier.BaseUrl
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.Cache
import okhttp3.Dispatcher
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val MAX_REQUESTS_PER_HOST = 100
    private const val MAX_REQUESTS = 200
    private const val CONNECT_TIMEOUT_SECONDS = 10L
    private const val READ_TIMEOUT_SECONDS = 30L
    private const val WRITE_TIMEOUT_SECONDS = 15L
    private const val CACHE_SIZE_BYTES = 20L * 1024L * 1024L

    @Provides
    @BaseUrl
    fun providesBaseUrl(): String = "https://comicvine.gamespot.com/api/"

    @Provides
    @ApiKey
    fun providesApiKey(): String = BuildConfig.COMIC_VINE_API_KEY

    @Provides
    @Singleton
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        coerceInputValues = true
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        comicVineAuthInterceptor: ComicVineAuthInterceptor,
        cache: Cache,
    ): OkHttpClient {
        val dispatcher = Dispatcher().apply {
            maxRequestsPerHost = MAX_REQUESTS_PER_HOST
            maxRequests = MAX_REQUESTS
        }
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        val builder = OkHttpClient.Builder()
        if (BuildConfig.DEBUG) {
            builder.addInterceptor(loggingInterceptor)
        }
        builder.addInterceptor(comicVineAuthInterceptor)
        return builder
            .dispatcher(dispatcher)
            .cache(cache)
            .connectTimeout(CONNECT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(READ_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun providesHttpCache(@ApplicationContext context: Context): Cache =
        Cache(context.cacheDir, CACHE_SIZE_BYTES)

    @Provides
    @Singleton
    fun provideRetrofit(
        @BaseUrl baseUrl: String,
        okHttpClient: OkHttpClient,
        json: Json,
    ): Retrofit {
        val contentType = "application/json".toMediaType()
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
    }

    @Provides
    @Singleton
    fun provideCharacterApiService(retrofit: Retrofit): CharacterService {
        return retrofit.create(CharacterService::class.java)
    }
}
