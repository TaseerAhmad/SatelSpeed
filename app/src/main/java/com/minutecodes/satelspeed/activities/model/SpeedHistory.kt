package com.minutecodes.satelspeed.activities.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.minutecodes.satelspeed.activities.HISTORY_TABLE

@Entity(tableName = HISTORY_TABLE)
data class SpeedHistory(
    @PrimaryKey(autoGenerate = true) var historyId: Int? = null,
    var historyName: String = "",
    var arriveAddress: String = "",
    var departAddress: String = "",
    var duration: String = "",
    var historyDate: String = "",
    var distanceCovered: String = "",
    var averageSpeed: String = "",
    var topSpeed: String = ""
)