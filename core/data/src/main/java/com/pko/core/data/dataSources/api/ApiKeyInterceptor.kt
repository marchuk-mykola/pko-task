package com.pko.core.data.dataSources.api

import com.pko.core.data.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class ApiKeyInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val url = originalRequest.url
            .newBuilder()
            .addQueryParameter("api_key", BuildConfig.apiKey)
            .build()
        val request = originalRequest
            .newBuilder()
            .url(url)
            .build()
        return chain.proceed(request)
    }

}