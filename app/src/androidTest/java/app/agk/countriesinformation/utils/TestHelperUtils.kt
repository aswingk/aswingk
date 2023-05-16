package app.agk.countriesinformation.utils

import app.agk.countriesinformation.data.source.local.CountryInfo

fun getCountryInfo() = CountryInfo(
        id = "abc",
        name = "country1",
        population = 1_000L,
        capital = listOf(),
        area = 1230.0,
        region = "region",
        subregion = "subregion",
        mapInfoUrl = "mapUrl"
)