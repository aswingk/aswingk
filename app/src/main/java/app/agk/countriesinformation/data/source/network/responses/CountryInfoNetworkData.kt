package app.agk.countriesinformation.data.source.network.responses

data class MapsInfo(val googleMaps: String)

data class CountryInfoNetworkData(
    val capital : List<String>,
    val population : Long,
    val area : Double,
    val region : String,
    val subregion : String,
    val maps : MapsInfo
)