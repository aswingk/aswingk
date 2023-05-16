package app.agk.countriesinformation.data.source.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CountryInfo(
    @PrimaryKey
    val id : String,
    val name: String = "Name",

    val capital : List<String>,
    val population : Long = 20000L,
    val area : Double = 34242.56,
    val region : String,
    val subregion : String,
    val mapInfoUrl : String
){
    override fun toString(): String {
        return name
    }
}