package app.agk.countriesinformation.data.source.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface CountryDao {
    @Upsert
    suspend fun upsert(countryInfo: CountryInfo)

    @Query("SELECT * FROM COUNTRYINFO WHERE name=:name")
    fun getCountryInfo(name: String): Flow<CountryInfo?>
}