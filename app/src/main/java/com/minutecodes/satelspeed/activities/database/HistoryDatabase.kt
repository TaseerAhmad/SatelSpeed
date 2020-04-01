package com.minutecodes.satelspeed.activities.database

import android.app.Application
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.minutecodes.satelspeed.activities.DB_NAME
import com.minutecodes.satelspeed.activities.DB_VERSION
import com.minutecodes.satelspeed.activities.dao.HistoryDao
import com.minutecodes.satelspeed.activities.model.SpeedHistory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [SpeedHistory::class], version = DB_VERSION)
abstract class HistoryDatabase : RoomDatabase() {
    abstract fun historyDao(): HistoryDao

    companion object {
        private lateinit var database: HistoryDatabase

        fun getInstance(application: Application): HistoryDatabase {
            if (!::database.isInitialized) {
                synchronized(HistoryDatabase::class.java) {
                    database = initRoomDb(application)
                }
            }
            return database
        }

        private fun initRoomDb(application: Application): HistoryDatabase {
            return Room.databaseBuilder(application, HistoryDatabase::class.java, DB_NAME)
                .fallbackToDestructiveMigration()
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        Log.d("ONCREATE_DB","INVOKED")
                        CoroutineScope(Dispatchers.IO).launch {
                            getInstance(application).historyDao().apply {
                                insert(SpeedHistory(historyName = "History 01",
                                    arriveAddress = "47 Berkshire Road\n" +
                                            "Concord, NH 03301",
                                    departAddress = "494 Green St.\n" +
                                            "Crawfordsville, IN 47933", duration = "1 h 31 m",
                                    distanceCovered = "132 Km",
                                    averageSpeed = "85 Km/h",
                                    historyDate = "2-01-2020",
                                    topSpeed = "122 Km/h"))
                                insert(SpeedHistory(historyName = "History 01",
                                    arriveAddress = "47 Berkshire Road\n" +
                                            "Concord, NH 03301",
                                    departAddress = "494 Green St.\n" +
                                            "Crawfordsville, IN 47933", duration = "1 h 31 m",
                                    distanceCovered = "132 Km",
                                    averageSpeed = "85 Km/h",
                                    historyDate = "2-01-2020",
                                    topSpeed = "122 Km/h"))
                                insert(SpeedHistory(historyName = "History 01",
                                    arriveAddress = "47 Berkshire Road\n" +
                                            "Concord, NH 03301",
                                    departAddress = "494 Green St.\n" +
                                            "Crawfordsville, IN 47933", duration = "1 h 31 m",
                                    distanceCovered = "132 Km",
                                    averageSpeed = "85 Km/h",
                                    historyDate = "2-01-2020",
                                    topSpeed = "122 Km/h"))
                                insert(SpeedHistory(historyName = "History 01",
                                    arriveAddress = "47 Berkshire Road\n" +
                                            "Concord, NH 03301",
                                    departAddress = "494 Green St.\n" +
                                            "Crawfordsville, IN 47933", duration = "1 h 31 m",
                                    distanceCovered = "132 Km",
                                    averageSpeed = "85 Km/h",
                                    historyDate = "2-01-2020",
                                    topSpeed = "122 Km/h"))
                                insert(SpeedHistory(historyName = "History 01",
                                    arriveAddress = "47 Berkshire Road\n" +
                                            "Concord, NH 03301",
                                    departAddress = "494 Green St.\n" +
                                            "Crawfordsville, IN 47933", duration = "1 h 31 m",
                                    distanceCovered = "132 Km",
                                    averageSpeed = "85 Km/h",
                                    historyDate = "2-01-2020",
                                    topSpeed = "122 Km/h"))

                                insert(SpeedHistory(historyName = "History 01",
                                    arriveAddress = "47 Berkshire Road\n" +
                                            "Concord, NH 03301",
                                    departAddress = "494 Green St.\n" +
                                            "Crawfordsville, IN 47933", duration = "1 h 31 m",
                                    distanceCovered = "132 Km",
                                    averageSpeed = "85 Km/h",
                                    historyDate = "2-01-2020",
                                    topSpeed = "122 Km/h"))

                                insert(SpeedHistory(historyName = "History 01",
                                    arriveAddress = "47 Berkshire Road\n" +
                                            "Concord, NH 03301",
                                    departAddress = "494 Green St.\n" +
                                            "Crawfordsville, IN 47933", duration = "1 h 31 m",
                                    distanceCovered = "132 Km",
                                    averageSpeed = "85 Km/h",
                                    historyDate = "2-01-2020",
                                    topSpeed = "122 Km/h"))

                                insert(SpeedHistory(historyName = "History 01",
                                    arriveAddress = "47 Berkshire Road\n" +
                                            "Concord, NH 03301",
                                    departAddress = "494 Green St.\n" +
                                            "Crawfordsville, IN 47933", duration = "1 h 31 m",
                                    distanceCovered = "132 Km",
                                    averageSpeed = "85 Km/h",
                                    historyDate = "2-01-2020",
                                    topSpeed = "122 Km/h"))

                                insert(SpeedHistory(historyName = "History 01",
                                    arriveAddress = "47 Berkshire Road\n" +
                                            "Concord, NH 03301",
                                    departAddress = "494 Green St.\n" +
                                            "Crawfordsville, IN 47933", duration = "1 h 31 m",
                                    distanceCovered = "132 Km",
                                    averageSpeed = "85 Km/h",
                                    historyDate = "2-01-2020",
                                    topSpeed = "122 Km/h"))

                                insert(SpeedHistory(historyName = "History 01",
                                    arriveAddress = "47 Berkshire Road\n" +
                                            "Concord, NH 03301",
                                    departAddress = "494 Green St.\n" +
                                            "Crawfordsville, IN 47933", duration = "1 h 31 m",
                                    distanceCovered = "132 Km",
                                    averageSpeed = "85 Km/h",
                                    historyDate = "2-01-2020",
                                    topSpeed = "122 Km/h"))

                                insert(SpeedHistory(historyName = "History 01",
                                    arriveAddress = "47 Berkshire Road\n" +
                                            "Concord, NH 03301",
                                    departAddress = "494 Green St.\n" +
                                            "Crawfordsville, IN 47933", duration = "1 h 31 m",
                                    distanceCovered = "132 Km",
                                    averageSpeed = "85 Km/h",
                                    historyDate = "2-01-2020",
                                    topSpeed = "122 Km/h"))

                                insert(SpeedHistory(historyName = "History 01",
                                    arriveAddress = "47 Berkshire Road\n" +
                                            "Concord, NH 03301",
                                    departAddress = "494 Green St.\n" +
                                            "Crawfordsville, IN 47933", duration = "1 h 31 m",
                                    distanceCovered = "132 Km",
                                    averageSpeed = "85 Km/h",
                                    historyDate = "2-01-2020",
                                    topSpeed = "122 Km/h"))

                                insert(SpeedHistory(historyName = "History 01",
                                    arriveAddress = "47 Berkshire Road\n" +
                                            "Concord, NH 03301",
                                    departAddress = "494 Green St.\n" +
                                            "Crawfordsville, IN 47933", duration = "1 h 31 m",
                                    distanceCovered = "132 Km",
                                    averageSpeed = "85 Km/h",
                                    historyDate = "2-01-2020",
                                    topSpeed = "122 Km/h"))

                                insert(SpeedHistory(historyName = "History 01",
                                    arriveAddress = "47 Berkshire Road\n" +
                                            "Concord, NH 03301",
                                    departAddress = "494 Green St.\n" +
                                            "Crawfordsville, IN 47933", duration = "1 h 31 m",
                                    distanceCovered = "132 Km",
                                    averageSpeed = "85 Km/h",
                                    historyDate = "2-01-2020",
                                    topSpeed = "122 Km/h"))

                                insert(SpeedHistory(historyName = "History 01",
                                    arriveAddress = "47 Berkshire Road\n" +
                                            "Concord, NH 03301",
                                    departAddress = "494 Green St.\n" +
                                            "Crawfordsville, IN 47933", duration = "1 h 31 m",
                                    distanceCovered = "132 Km",
                                    averageSpeed = "85 Km/h",
                                    historyDate = "2-01-2020",
                                    topSpeed = "122 Km/h"))

                                insert(SpeedHistory(historyName = "History 01",
                                    arriveAddress = "47 Berkshire Road\n" +
                                            "Concord, NH 03301",
                                    departAddress = "494 Green St.\n" +
                                            "Crawfordsville, IN 47933", duration = "1 h 31 m",
                                    distanceCovered = "132 Km",
                                    averageSpeed = "85 Km/h",
                                    historyDate = "2-01-2020",
                                    topSpeed = "122 Km/h"))

                                insert(SpeedHistory(historyName = "History 01",
                                    arriveAddress = "47 Berkshire Road\n" +
                                            "Concord, NH 03301",
                                    departAddress = "494 Green St.\n" +
                                            "Crawfordsville, IN 47933", duration = "1 h 31 m",
                                    distanceCovered = "132 Km",
                                    averageSpeed = "85 Km/h",
                                    historyDate = "2-01-2020",
                                    topSpeed = "122 Km/h"))
                            }
                        }
                    }
                })
                .build()
        }
    }
}