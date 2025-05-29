package com.gitz.simplenotes.model

import com.google.gson.annotations.SerializedName
data class QuoteResponseItem(
    val quote: String? = null,
    val author: String? = null,
    val category: String? = null
)