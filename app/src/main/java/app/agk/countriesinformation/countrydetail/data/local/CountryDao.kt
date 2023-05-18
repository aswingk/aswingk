package app.agk.countriesinformation.countrydetail.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface CountryDao {
    @Upsert
    suspend fun upsert(countryInfo: CountryInfo)

    @Query("SELECT * FROM ${CountryInfo.TABLE_COUNTRIES} WHERE name=:name")
    fun getCountryInfo(name: String): Flow<CountryInfo?>
}