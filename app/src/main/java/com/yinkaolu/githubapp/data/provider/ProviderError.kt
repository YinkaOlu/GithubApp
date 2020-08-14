package com.yinkaolu.githubapp.data.provider

enum class ProviderErrorType {
    NOT_FOUND, FAILED
}

/**
 * Class containing information on data through in Data Provider
 */
class ProviderError(val type: ProviderErrorType, val message: String?="")