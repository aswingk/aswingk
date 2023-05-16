package app.agk.countriesinformation.data.source.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import app.agk.countriesinformation.utils.ListConverters

@Database(entities = [CountryInfo::class], version = 1)
@TypeConverters(ListConverters::class)
abstract class CountryDatabase : RoomDatabase() {
    abstract fun getCountryDao(): CountryDao

    companion object {
        @Volatile
        private var INSTANCE: CountryDatabase? = null
        fun getInstance(context: Context): CountryDatabase {
            if (INSTANCE == null) {
                synchronized(this) {
                    if (INSTANCE == null)
                        Room
                            .databaseBuilder(
                                context.applicationContext,
                                CountryDatabase::class.java,
                                "country_db"
                            )
                            .fallbackToDestructiveMigration()
                            .build().also {
                                INSTANCE = it
                            }
                }
            }
            return INSTANCE!!
        }
    }
}