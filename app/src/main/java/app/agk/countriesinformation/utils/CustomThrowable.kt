package app.agk.countriesinformation.utils

class CustomThrowable(val errorState: ErrorState, throwable : Throwable) : Throwable(throwable)

enum class ErrorState {
    DatabaseError,
    NoNetworkError,
    HttpError
}