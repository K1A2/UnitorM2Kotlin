package com.unitor.k1a2.unitorm.view.Recycler.listener

import android.content.Context
import android.view.MotionEvent
import android.support.v7.widget.RecyclerView
import android.text.method.Touch.onTouchEvent
import android.util.Log
import android.view.GestureDetector
import android.view.View


class RecyclerItemClickListener(context: Context, recyclerView: RecyclerView, private val onItemClickListener: OnItemClickListener?) : RecyclerView.OnItemTouchListener {

    internal var gestureDetector: GestureDetector

    interface OnItemClickListener {
        fun onItemClicked(view: View, position: Int)

        fun onLongItemClicked(view: View?, position: Int)
    }

    init {
        gestureDetector = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
            override fun onSingleTapUp(e: MotionEvent): Boolean {
                return true
            }

            override fun onLongPress(e: MotionEvent) {
                val child = recyclerView.findChildViewUnder(e.x, e.y)
                if (child != null && onItemClickListener != null) {
                    Log.d("long", "press")
                    onItemClickListener.onLongItemClicked(child, recyclerView.getChildAdapterPosition(child))
                }
            }
        })
    }

    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
        val child = rv.findChildViewUnder(e.x, e.y)
        if (child != null && onItemClickListener != null && gestureDetector.onTouchEvent(e)) {
            onItemClickListener.onItemClicked(child, rv.getChildAdapterPosition(child))
            return true
        }
        return false
    }

    override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {

    }

    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {

    }
}