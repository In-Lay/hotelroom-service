package com.inlay.hotelroomservice.presentation.view
//
//import android.content.Context
//import android.util.AttributeSet
//import android.view.View
//import androidx.coordinatorlayout.widget.CoordinatorLayout
//import com.google.android.material.appbar.AppBarLayout
//import com.google.android.material.search.SearchBar
//import kotlin.math.abs
//
//class SearchBarBehavior(context: Context, attrs: AttributeSet) :
//    CoordinatorLayout.Behavior<SearchBar>(context, attrs) {
//    private var maxTranslationY: Float = 0f
//
//    override fun layoutDependsOn(
//        parent: CoordinatorLayout,
//        child: SearchBar,
//        dependency: View
//    ): Boolean {
//        return dependency is AppBarLayout
//    }
//
//    override fun onDependentViewChanged(
//        parent: CoordinatorLayout,
//        child: SearchBar,
//        dependency: View
//    ): Boolean {
//        if (dependency is AppBarLayout) updateSearchBarPosition(child, dependency)
//        return true
//    }
//
//    private fun updateSearchBarPosition(searchBar: SearchBar, appBarLayout: AppBarLayout) {
//        val expandedRange = appBarLayout.totalScrollRange.toFloat()
//        val verticalOffset = abs(appBarLayout.top).toFloat()
//        val collapseRatio = verticalOffset / expandedRange
//
//        val searchBarLayoutParams = searchBar.layoutParams as AppBarLayout.LayoutParams
//
//        if (maxTranslationY == 0F) {
//            val homeButtonWidth = searchBar.supportToolbar.navigationIcon?.intrinsicWidth ?: 0
//        }
//    }
//}