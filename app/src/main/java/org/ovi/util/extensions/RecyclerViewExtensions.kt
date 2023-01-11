package org.ovi.util.extensions

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import kotlin.math.min


fun ViewGroup.inflater(layoutRes: Int): View =
    LayoutInflater.from(context).inflate(layoutRes, this, false)


fun RecyclerView.onLoadMoreListener(extraCount: Int = 0, onLoadMore: () -> Unit) {

    this.addOnScrollListener(object : RecyclerView.OnScrollListener() {

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            if (dy < 0) return

            if (layoutManager == null) return

            val totalItemCount = layoutManager!!.itemCount

            val visibleTotalCount = layoutManager!!.childCount
            val firstVisibleItem = when (layoutManager) {

                is GridLayoutManager -> {
                    (layoutManager as GridLayoutManager).findFirstVisibleItemPosition()
                }

                is LinearLayoutManager -> {
                    (layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                }

                is StaggeredGridLayoutManager -> {
                    val staggeredGridLayoutManager =
                        layoutManager as StaggeredGridLayoutManager
                    val spanCount = staggeredGridLayoutManager.spanCount
                    val lastPositions =
                        staggeredGridLayoutManager.findFirstVisibleItemPositions(IntArray(spanCount))
                    min(lastPositions[0], lastPositions[1])
                }

                else -> 0

            }

            if (totalItemCount <= visibleTotalCount) {
                return
            }

            if (firstVisibleItem + visibleTotalCount + extraCount >= totalItemCount) {
                onLoadMore.invoke()
            }

        }

    })

}
