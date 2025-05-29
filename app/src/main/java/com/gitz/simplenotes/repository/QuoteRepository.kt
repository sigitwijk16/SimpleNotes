package com.gitz.simplenotes.repository

import com.gitz.simplenotes.model.QuoteResponseItem
import retrofit2.Response

interface QuoteRepository {
    suspend fun getRandomQuote(): Response<List<QuoteResponseItem>>
}