package app.agk.countriesinformation.countrydetail.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = CountryInfo.TABLE_COUNTRIES)
data class CountryInfo(
    @PrimaryKey
    val id : String,
    val name: String,
    val capital : List<String>,
    val population : Long,
    val area : Double,
    val region : String,
    val subregion : String,
    val mapInfoUrl : String
){
    companion object {
        const val TABLE_COUNTRIES = "CountryInfo"
    }
}