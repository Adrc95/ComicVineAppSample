package com.adrc95.core.network.di

import android.content.Context
import com.adrc95.core.network.BuildConfig
import com.adrc95.core.network.CacheSizeBytes
import com.adrc95.core.network.ConnectTimeoutSeconds
import com.adrc95.core.network.MaxRequests
import com.adrc95.core.network.MaxRequestsPerHost
import com.adrc95.core.network.ReadTimeoutSeconds
import com.adrc95.core.network.WriteTimeoutSeconds
import com.adrc95.core.network.datasource.ComicVineCharactersDataSource
import com.adrc95.core.network.di.qualifier.ApiKey
import com.adrc95.core.network.service.CharacterService
import com.adrc95.core.network.di.qualifier.BaseUrl
import com.adrc95.core.network.interceptor.ComicVineAuthInterceptor
import com.adrc95.data.datasource.RemoteCharactersDataSource
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.Cache
import okhttp3.Dispatcher
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {



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
            maxRequestsPerHost = MaxRequestsPerHost
            maxRequests = MaxRequests
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
            .connectTimeout(ConnectTimeoutSeconds, TimeUnit.SECONDS)
            .readTimeout(ReadTimeoutSeconds, TimeUnit.SECONDS)
            .writeTimeout(WriteTimeoutSeconds, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun providesHttpCache(@ApplicationContext context: Context): Cache =
        Cache(context.cacheDir, CacheSizeBytes)

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

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {
    @Binds
    abstract fun bindCharactersDataSource(remote: ComicVineCharactersDataSource): RemoteCharactersDataSource
}
