package com.moviles.vetconnect.entities

data class LoginUiState(
    val loading: Boolean = false,
    val success: Boolean = false,
    val role: String? = null,
    val error: String? = null
)

