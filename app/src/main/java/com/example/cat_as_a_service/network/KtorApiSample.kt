package com.example.cat_as_a_service.network

import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.features.get
import io.ktor.client.request.*
import io.ktor.client.statement.*

class KtorApiSample(private val client: HttpClient) {
    suspend fun getSampleResponse(): String = client.get("asdasd")
}