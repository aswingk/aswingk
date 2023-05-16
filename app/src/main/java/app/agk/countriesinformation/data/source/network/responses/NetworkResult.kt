package app.agk.countriesinformation.data.source.network.responses

sealed class NetworkResult<out T> {
    data class Success<out T>(val value: T) : NetworkResult<T>()
    data class Failure(
        val isNetworkError: Boolean,
        val networkErrorCode : Int? = null,
        val networkErrorBody : String? = null
    ) : NetworkResult<Nothing>()
}