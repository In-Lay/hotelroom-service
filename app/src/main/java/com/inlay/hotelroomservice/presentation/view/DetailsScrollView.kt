package com.inlay.hotelroomservice.presentation.view

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.core.widget.NestedScrollView

class DetailsScrollView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null
) : NestedScrollView(context, attributeSet) {
    private var shouldStopInterceptingTouchEvent = false

    fun setShouldInterceptTouchEvent(shouldStopInterceptingTouchEvent: Boolean) {
        this.shouldStopInterceptingTouchEvent = shouldStopInterceptingTouchEvent
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        return if (shouldStopInterceptingTouchEvent) false
        else
            super.onInterceptTouchEvent(ev)
    }
}