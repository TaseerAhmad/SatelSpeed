package com.minutecodes.satelspeed.activities.helpers

import android.view.View

private const val EMPTY_VIEWS_EXCEPTION =
    "No associated View object found. Call addSwitchableView() method to add a view."

class SelectionViewSwitcher {
    private var focusedViewIndex = 0
    private val viewList = ArrayList<View>()
    private lateinit var viewSwitchListener: ViewSwitchListener

    interface ViewSwitchListener {
        fun onViewEndReached()
        fun onViewStartReached()
    }

    fun previousView() {
        if (viewList.isEmpty())
            throwNoViewFoundException()

        if (hasStartReached()) {
            notifyBeginReachedListener()
            return
        }

        val focusedView = viewList[focusedViewIndex]
        val previousView = viewList[--focusedViewIndex]
        focusedView.visibility = View.GONE
        previousView.visibility = View.VISIBLE
    }

    fun nextView() {
        if (viewList.isEmpty())
            throwNoViewFoundException()

        if (hasEndReached()) {
            notifyEndReachedListener()
            return
        }

        val focusedView = viewList[focusedViewIndex]
        val nextView = viewList[++focusedViewIndex]
        focusedView.visibility = View.GONE
        nextView.visibility = View.VISIBLE
    }

    fun getFocusedViewTag(): Int {
        if (viewList.isEmpty())
            throwNoViewFoundException()

        return focusedViewIndex
    }

    fun makeViewVisible(id: Int) {
        if (viewList.isEmpty())
            throwNoViewFoundException()

        if (id in 0..viewList.lastIndex) {
            flushViews()
            focusedViewIndex = id
            viewList.elementAt(id).visibility = View.VISIBLE
        }
    }

    fun makeViewVisible(v: View) {
        if (viewList.isEmpty())
            throwNoViewFoundException()

        if (viewList.isNotEmpty() && viewList.contains(v)) {
            flushViews()
            val index = viewList.indexOf(v)
            if (index == -1)
                throwNoViewFoundException()

            focusedViewIndex = index
            viewList[index].visibility = View.VISIBLE
        }
    }

    fun clearAllViews() {
        viewList.clear()
    }

    fun addListener(listener: ViewSwitchListener) {
        viewSwitchListener = listener
    }

    fun addSwitchableView(v: View) {
        if (!viewList.contains(v)) {
            viewList.add(v)
        }
    }

    private fun flushViews() {
        viewList.forEach {
            it.visibility = View.GONE
        }
    }

    private fun hasEndReached(): Boolean {
        return focusedViewIndex == viewList.lastIndex
    }

    private fun hasStartReached(): Boolean {
        return focusedViewIndex == 0
    }

    private fun notifyEndReachedListener() {
        if (::viewSwitchListener.isInitialized)
            viewSwitchListener.onViewEndReached()
    }

    private fun notifyBeginReachedListener() {
        if (::viewSwitchListener.isInitialized)
            viewSwitchListener.onViewStartReached()
    }

    private fun throwNoViewFoundException() {
        throw RuntimeException(EMPTY_VIEWS_EXCEPTION)
    }
}