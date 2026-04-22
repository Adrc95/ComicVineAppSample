package com.adrc95.comicvineappsample.ui.common

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch


fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = true): View =
    LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)


fun ImageView.loadUrl(url: String) {
    Glide.with(context).load(url.replace("http:","https:")).into(this)
}

inline fun <reified T : Activity> Context.intentFor(body: Intent.() -> Unit): Intent =
    Intent(this, T::class.java).apply(body)

inline fun <reified T : Activity> Context.startActivity(body: Intent.() -> Unit) {
    startActivity(intentFor<T>(body))
}

fun View.setVisible(visible: Boolean) {
    visibility = if (visible) View.VISIBLE else View.GONE
}

fun View.isVisible() = visibility == View.VISIBLE

inline fun <T : Any> basicDiffUtil(
    crossinline areItemsTheSame: (T, T) -> Boolean = { old, new -> old == new },
    crossinline areContentsTheSame: (T, T) -> Boolean = { old, new -> old == new }
) = object : DiffUtil.ItemCallback<T>() {
    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean =
        areItemsTheSame(oldItem, newItem)

    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean =
        areContentsTheSame(oldItem, newItem)
}

fun RecyclerView.setDivider(@DrawableRes drawableRes: Int) {
    val divider = DividerItemDecoration(
        this.context,
        DividerItemDecoration.VERTICAL
    )
    val drawable = ContextCompat.getDrawable(
        this.context,
        drawableRes
    )
    drawable?.let {
        divider.setDrawable(it)
        addItemDecoration(divider)
    }
}

fun <T> LifecycleOwner.launchAndCollect(
    flow: Flow<T>,
    state: Lifecycle.State = Lifecycle.State.STARTED,
    body: (T) -> Unit
) {
    lifecycleScope.launch {
        this@launchAndCollect.repeatOnLifecycle(state) {
            flow.collect(body)
        }
    }
}

fun RecyclerView.addOnScrolledToEnd(onScrolledToEnd: (Int) -> Unit) {
    clearOnScrollListeners()
    addOnScrollListener(object : RecyclerView.OnScrollListener() {
        // The minimum amount of items to have below your current scroll position
        // before loading more.
        private val VISIBLE_THRESHOLD = 5
        // True if we are still waiting for the last set of data to load.
        private var loading = true
        // The total number of items in the dataset after the last load
        private var previousTotal = 0

        private fun getLastVisibleItem(lastVisibleItemPositions: IntArray): Int {
            var maxSize = 0
            for (i in lastVisibleItemPositions.indices) {
                if (i == 0) {
                    maxSize = lastVisibleItemPositions[i]
                } else if (lastVisibleItemPositions[i] > maxSize) {
                    maxSize = lastVisibleItemPositions[i]
                }
            }
            return maxSize
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            if (dy <= 0) return

            val visibleThreshold = when (layoutManager) {
                is GridLayoutManager -> {
                    val layoutManager = layoutManager as GridLayoutManager
                    VISIBLE_THRESHOLD * layoutManager.spanCount
                }
                is StaggeredGridLayoutManager -> {
                    val layoutManager = layoutManager as StaggeredGridLayoutManager
                    VISIBLE_THRESHOLD * layoutManager.spanCount
                }
                else -> {
                    VISIBLE_THRESHOLD
                }
            }

            val visibleItemCount = layoutManager?.childCount ?: 0
            val totalItemCount = layoutManager?.itemCount ?: 0
            val firstVisibleItem = when (layoutManager) {
                is LinearLayoutManager -> {
                    val layoutManager = layoutManager as LinearLayoutManager
                    layoutManager.findFirstVisibleItemPosition()
                }
                is GridLayoutManager -> {
                    val layoutManager = layoutManager as GridLayoutManager
                    layoutManager.findFirstVisibleItemPosition()
                }
                is StaggeredGridLayoutManager -> {
                    val layoutManager = layoutManager as StaggeredGridLayoutManager
                    getLastVisibleItem(layoutManager.findLastVisibleItemPositions(null))
                }
                else -> {
                    throw IllegalStateException("Invalid LayoutManager")
                }
            }

            if (loading && totalItemCount > previousTotal) {
                loading = false
                previousTotal = totalItemCount
            }

            if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
                onScrolledToEnd(totalItemCount)
                loading = true
            }
        }
    })

}
