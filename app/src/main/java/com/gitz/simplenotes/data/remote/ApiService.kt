package com.gitz.simplenotes.data.remote

import com.gitz.simplenotes.model.QuoteResponseItem
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET("v1/quotes")
    suspend fun getRandomQuote(): Response<List<QuoteResponseItem>>
}