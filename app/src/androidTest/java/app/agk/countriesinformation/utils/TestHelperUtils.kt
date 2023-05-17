package app.agk.countriesinformation.utils

import app.agk.countriesinformation.data.source.local.CountryInfo
import java.util.*

private fun generateId() = UUID.randomUUID().toString()

fun getCountryInfo() = CountryInfo(
        id = generateId(),
        name = "USA",
        population = 1_000_000L,
        capital = listOf("Atlanta"),
        area = 1230.0,
        region = "Georgia",
        subregion = "Peachtree Corners",
        mapInfoUrl = "mapUrl"
)