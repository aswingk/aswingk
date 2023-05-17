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

@Suppress("UNNECESSARY_SAFE_CALL")
fun CountryInfoNetworkData.asEntity() = CountryInfo(
    id = UUID.randomUUID().toString(),
    name = name?.common ?: "",
    capital = capital ?: listOf(),
    population = population ?: 0L,
    area = area ?: 0.0,
    region = region ?: "",
    subregion = subregion ?: "",
    mapInfoUrl = maps?.googleMaps?: ""
)