package app.agk.countriesinformation.utils

import androidx.room.TypeConverter
import com.google.gson.Gson

class ListConverters {
    @TypeConverter
    fun fromString(value: String) : List<String> =
        Gson().fromJson(value, Array<String>::class.java).toList()

    @TypeConverter
    fun toJsonString(list: List<String>) = Gson().toJson(list)
}