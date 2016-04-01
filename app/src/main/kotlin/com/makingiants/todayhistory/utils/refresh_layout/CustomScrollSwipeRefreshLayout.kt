package com.makingiants.todayhistory.utils.refresh_layout

import android.content.Context
import android.support.v4.widget.SwipeRefreshLayout
import android.util.AttributeSet

/**
 * Use [ScrollEnabler] to know when [CustomScrollSwipeRefreshLayout] can scroll up.
 * It's usefull when the mView have multiple inner views that are not inside a [ScrollView]
 */
class CustomScrollSwipeRefreshLayout(context: Context, attrs: AttributeSet) : SwipeRefreshLayout(context, attrs) {
    private var mScrollEnabler: ScrollEnabler? = null

    fun setScrollEnabler(scrollEnabler: ScrollEnabler?) {
        mScrollEnabler = scrollEnabler
    }

    override fun canChildScrollUp(): Boolean {
        if (mScrollEnabler != null) {
            return mScrollEnabler!!.canScrollUp()
        }
        return super.canChildScrollUp()
    }
}
