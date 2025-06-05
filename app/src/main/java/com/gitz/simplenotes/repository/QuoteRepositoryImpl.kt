package com.gitz.simplenotes.repository

import com.gitz.simplenotes.data.remote.ApiService
import com.gitz.simplenotes.model.QuoteResponseItem
import retrofit2.Response
import javax.inject.Inject

class QuoteRepositoryImpl(private val apiService: ApiService) : QuoteRepository {
    override suspend fun getRandomQuote(): Response<List<QuoteResponseItem>> {
        return apiService.getRandomQuote()
    }
}