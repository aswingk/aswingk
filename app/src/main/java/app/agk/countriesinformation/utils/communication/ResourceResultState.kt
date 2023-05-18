package app.agk.countriesinformation.models.communication

import app.agk.countriesinformation.utils.CustomThrowable

sealed class ResourceResultState {
    data class Success(val value: Country) : ResourceResultState()
    data class Failure(
        val customThrowable: CustomThrowable
    ) : ResourceResultState()
    class Loading() : ResourceResultState()
}