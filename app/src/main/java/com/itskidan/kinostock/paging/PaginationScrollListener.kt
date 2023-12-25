package com.itskidan.kinostock.paging

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class PaginationScrollListener(private val layoutManager: LinearLayoutManager) :
    RecyclerView.OnScrollListener() {
    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        val visibleItemCount: Int = layoutManager.childCount
        val totalItemCount: Int = layoutManager.itemCount
        val firstVisibleItemPosition: Int = layoutManager.findFirstVisibleItemPosition()
        val lastVisibleItem = layoutManager.findLastVisibleItemPosition()
        val visibleThreshold = 3
        if (!isLoading() && !isLastPage()) {
            if (totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                loadMoreItems()
            }
        }
    }

    protected abstract fun loadMoreItems()
    abstract fun isLastPage(): Boolean
    abstract fun isLoading(): Boolean
}