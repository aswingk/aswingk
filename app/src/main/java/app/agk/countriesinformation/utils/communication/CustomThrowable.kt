package app.agk.countriesinformation.utils

class CustomThrowable(val errorState: ErrorState, throwable : Throwable) : Throwable(throwable)

enum class ErrorState {
    LocalDataSourceError,
    NoNetworkError,
    HttpError
}