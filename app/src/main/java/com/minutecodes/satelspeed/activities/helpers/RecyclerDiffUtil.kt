package com.minutecodes.satelspeed.activities.helpers

import androidx.recyclerview.widget.DiffUtil
import com.minutecodes.satelspeed.activities.model.SpeedHistory

class RecyclerDiffUtil : DiffUtil.ItemCallback<SpeedHistory>() {

    override fun areItemsTheSame(oldItem: SpeedHistory, newItem: SpeedHistory): Boolean {
        return oldItem.historyId == newItem.historyId
    }

    override fun areContentsTheSame(oldItem: SpeedHistory, newItem: SpeedHistory): Boolean {
        return oldItem == newItem
    }
}