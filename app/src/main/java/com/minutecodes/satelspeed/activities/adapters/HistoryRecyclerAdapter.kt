package com.minutecodes.satelspeed.activities.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.minutecodes.satelspeed.R
import com.minutecodes.satelspeed.activities.helpers.RecyclerDiffUtil
import com.minutecodes.satelspeed.activities.interfaces.java.RecyclerItemClickListener
import com.minutecodes.satelspeed.activities.model.SpeedHistory

//private const val TOGGLE_FACING_DOWN = 270F
//private const val TOGGLE_FACING_UP = -270F

class HistoryRecyclerAdapter(clickListener: RecyclerItemClickListener) :
    ListAdapter<SpeedHistory, HistoryRecyclerAdapter.ItemViewHolder>(RecyclerDiffUtil()) {
    private val recyclerItemClickListener = clickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(getInflatedView(parent), recyclerItemClickListener)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bindItem(currentList[position], position)
    }

    private fun getInflatedView(parent: ViewGroup): View {
        val inflater = LayoutInflater.from(parent.context)
        return inflater.inflate(R.layout.history_recycler_item_layout, parent, false)
    }

    class ItemViewHolder(
        private val view: View,
        private val clickListener: RecyclerItemClickListener
    ) : RecyclerView.ViewHolder(view), View.OnClickListener {
        private lateinit var historyDate: TextView
        private lateinit var historyTitle: TextView
        private lateinit var historyDepart: TextView
        private lateinit var historyArrive: TextView
        private lateinit var historyAverage: TextView
        private lateinit var historyDistance: TextView
        private lateinit var historyDuration: TextView
        private lateinit var historyMaxSpeed: TextView
//        private lateinit var cardItemHolder: View
//        private lateinit var cardToggleButton: View
//        private val sparseBooleanArray = SparseBooleanArray(18)

        init {
            findViews()
            view.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            v ?: return
            clickListener.onClick(view)
//            handleViewClick(v)
        }

        fun bindItem(speedHistory: SpeedHistory, position: Int) {
            //handleViewExpand(position)

            speedHistory.also {
                historyDate.text = it.historyDate
                historyTitle.text = it.historyName
                historyDuration.text = it.duration
                historyMaxSpeed.text = it.topSpeed
                historyDepart.text = it.departAddress
                historyArrive.text = it.arriveAddress
                historyAverage.text = it.averageSpeed
                historyDistance.text = it.distanceCovered
            }
        }

//        private fun handleViewClick(view: View) {
//            sparseBooleanArray.apply {
//                val position = adapterPosition
//                val isViewExpanded = !get(position, false)
//                set(position, isViewExpanded)
//                handleViewExpand(position)
//            }
//            clickListener.onClick(view)
//        }
//
//        private fun expandView(i: Int) {
//            Log.d("EXPAND_VIEW","INVOKED")
//            sparseBooleanArray[i] = true
//            cardItemHolder.visibility = View.VISIBLE
//            cardToggleButton.rotation = TOGGLE_FACING_UP
//        }
//
//        private fun contractView(i: Int) {
//            sparseBooleanArray[i] = false
//            cardItemHolder.visibility = View.GONE
//            cardToggleButton.rotation = TOGGLE_FACING_DOWN
//        }
//
//        private fun handleViewExpand(i: Int) {
//            Log.d("BIND_POSITION","$i")
//            val isViewExpanded = sparseBooleanArray[i, false].also { Log.d("CURRENT_PSO", it.toString()) }
//            if (isViewExpanded) {
//                expandView(i)
//            } else {
//                contractView(i)
//            }
//        }

        private fun findViews() {
            with(view) {
                historyDate = findViewById(R.id.historyDate)
                historyTitle = findViewById(R.id.historyTitle)
                historyArrive = findViewById(R.id.historyArrive)
                historyDepart = findViewById(R.id.historyDepart)
                historyAverage = findViewById(R.id.historyAverage)
                historyDistance = findViewById(R.id.historyDistance)
                historyDuration = findViewById(R.id.historyDuration)
                historyMaxSpeed = findViewById(R.id.historyMaxSpeed)
//                cardItemHolder = findViewById(R.id.itemDetailsHolder)
//                cardToggleButton = findViewById(R.id.cardVisibilityToggle)
            }
        }
    }

}