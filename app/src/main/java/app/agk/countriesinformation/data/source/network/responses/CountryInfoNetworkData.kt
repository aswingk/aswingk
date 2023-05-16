package app.agk.countriesinformation.data.source.network.responses

import app.agk.countriesinformation.data.source.local.CountryInfo
import java.util.UUID

data class NameDetails(val common : String)

data class MapsInfo(val googleMaps: String)

data class CountryInfoNetworkData(

    val name: NameDetails,

    val capital : List<String>,
    val population : Long = 20000L,
    val area : Double = 34242.56,
    val region : String,
    val subregion : String,

    val maps : MapsInfo
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