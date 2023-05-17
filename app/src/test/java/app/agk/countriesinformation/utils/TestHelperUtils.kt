package app.agk.countriesinformation.utils

import app.agk.countriesinformation.data.source.local.CountryInfo
import app.agk.countriesinformation.data.source.network.responses.CountryInfoNetworkData
import app.agk.countriesinformation.data.source.network.responses.MapsInfo
import app.agk.countriesinformation.data.source.network.responses.NameDetails
import java.util.UUID

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

fun getCountryInfoNetworkData() = CountryInfoNetworkData(
        name = NameDetails("USA"),
        area = 1230.0,
        subregion = "Peachtree Corners",
        region = "Georgia",
        population = 1_000_000L,
        capital = listOf("Atlanta"),
        maps = MapsInfo("mapUrl")
)