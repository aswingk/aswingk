package app.agk.countriesinformation.data

data class Country (
    val id : String,
    val name: String,
    val capital : String = "",
    val population : Long = 20000L,
    val area : Double = 34242.56,
    val region : String,
    val subregion : String,
    val mapInfoUrl : String
){
    override fun toString(): String {
        return id+name
    }
}