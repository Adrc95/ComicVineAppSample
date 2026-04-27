package com.adrc95.core.network.interceptor

import com.adrc95.core.network.FormatJson
import com.adrc95.core.network.HeaderUserAgent
import com.adrc95.core.network.ParamApiKey
import com.adrc95.core.network.ParamFormat
import com.adrc95.core.network.UserAgentValue
import com.adrc95.core.network.di.qualifier.ApiKey
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class ComicVineAuthInterceptor @Inject constructor(
    @param:ApiKey private val apiKey: String
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val defaultRequest = chain.request()
        val httpUrl = defaultRequest.url.newBuilder()
            .addQueryParameter(ParamApiKey, apiKey)
            .addQueryParameter(ParamFormat, FormatJson)
            .build()

        return chain.proceed(
            defaultRequest.newBuilder()
                .url(httpUrl)
                .header(HeaderUserAgent, UserAgentValue)
                .build()
        )
    }
}
