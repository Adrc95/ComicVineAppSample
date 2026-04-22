package com.adrc95.marvelappsample.data.server

import com.adrc95.marvelappsample.data.util.Utils
import okhttp3.Interceptor
import okhttp3.Response

class MarvelKeyInterceptor(private val publicKey: String, private val privateKey : String)
    : Interceptor {

    companion object {
        private const val PARAM_API_KEY = "apikey"
        private const val PARAM_TS = "ts"
        private const val PARAM_HASH = "hash"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val timeStamp = System.currentTimeMillis()
        val defaultRequest = chain.request()
        val defaultHttpUrl = defaultRequest.url
        val httpUrl = defaultHttpUrl.newBuilder()
            .addEncodedQueryParameter(PARAM_TS, timeStamp.toString())
            .addEncodedQueryParameter(PARAM_API_KEY, publicKey)
            .addEncodedQueryParameter(PARAM_HASH, generateMarvelHash(timeStamp, privateKey, publicKey))
            .build()
        val requestBuilder = defaultRequest.newBuilder().url(httpUrl)
        return chain.proceed(requestBuilder.build())
    }

    /**
     * Method to calculate the hash required in Marvel API.
     *
     * @param timeStamp Current timestamp
     * @param privateKey privateKey
     * @param publicKey publicKey
     */
    private fun generateMarvelHash(timeStamp: Long, privateKey: String, publicKey: String): String {
        val marvelHash = timeStamp.toString() + privateKey + publicKey
        return Utils.md5(marvelHash)
    }
}
