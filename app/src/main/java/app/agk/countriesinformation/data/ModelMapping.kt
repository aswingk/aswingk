@file:Suppress("UNNECESSARY_SAFE_CALL", "USELESS_ELVIS")

package app.agk.countriesinformation.data

import app.agk.countriesinformation.data.source.local.CountryInfo
import app.agk.countriesinformation.data.source.network.responses.CountryInfoNetworkData
import java.util.UUID

fun CountryInfo.asCountry() = Country(
    id = id,
    name = name,
    capital = capital?.joinToString(",") ?: "",
    population = population,
    area = area,
    region = region,
    subregion = subregion,
    mapInfoUrl = mapInfoUrl
)

fun CountryInfoNetworkData.asEntityWithName(countryName : String) = CountryInfo(
    id = UUID.randomUUID().toString(),
    name = countryName,
    capital = capital ?: listOf(),
    population = population ?: 0L,
    area = area ?: 0.0,
    region = region ?: "",
    subregion = subregion ?: "",
    mapInfoUrl = maps?.googleMaps?: ""
)