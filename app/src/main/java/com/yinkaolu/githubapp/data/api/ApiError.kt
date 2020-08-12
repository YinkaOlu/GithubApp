package com.yinkaolu.githubapp.data.api

enum class ApiErrorType {
    CANCELLED, NOT_FOUND, FAILED
}

class ApiError(val type: ApiErrorType, val message: String?="")