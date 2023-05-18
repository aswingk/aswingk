package app.agk.countriesinformation.data

import app.agk.countriesinformation.utils.CustomThrowable

sealed class Resource {
    data class Success(val value: Country) : Resource()
    data class Failure(
        val customThrowable: CustomThrowable
    ) : Resource()
    class Loading() : Resource()
}