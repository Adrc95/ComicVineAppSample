package com.adrc95.comicvineappsample.data.server

import com.adrc95.comicvineappsample.data.util.Constants.FORMAT_JSON
import com.adrc95.comicvineappsample.data.util.Constants.HEADER_USER_AGENT
import com.adrc95.comicvineappsample.data.util.Constants.PARAM_API_KEY
import com.adrc95.comicvineappsample.data.util.Constants.PARAM_FORMAT
import com.adrc95.comicvineappsample.data.util.Constants.USER_AGENT_VALUE
import com.adrc95.comicvineappsample.di.qualifier.ApiKey
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class ComicVineAuthInterceptor @Inject constructor(
    @param:ApiKey private val apiKey: String
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val defaultRequest = chain.request()
        val httpUrl = defaultRequest.url.newBuilder()
            .addQueryParameter(PARAM_API_KEY, apiKey)
            .addQueryParameter(PARAM_FORMAT, FORMAT_JSON)
            .build()

        return chain.proceed(
            defaultRequest.newBuilder()
                .url(httpUrl)
                .header(HEADER_USER_AGENT, USER_AGENT_VALUE)
                .build()
        )
    }
}
