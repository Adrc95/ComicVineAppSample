package com.adrc95.comicvineappsample.ui.main.adapter

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.adrc95.comicvineappsample.ui.common.addOnScrolledToEnd
import com.adrc95.comicvineappsample.ui.favorite.adapter.FavoriteCharactersAdapter
import com.adrc95.comicvineappsample.ui.model.CharacterDisplayModel

@BindingAdapter("items")
fun RecyclerView.setItems(characters: List<CharacterDisplayModel>?) {
    val items = characters ?: return
    when (val currentAdapter = adapter) {
        is CharactersAdapter -> currentAdapter.submitList(items)
        is FavoriteCharactersAdapter -> currentAdapter.submitList(items)
        else -> Unit
    }
}

@BindingAdapter("onMoreItems")
fun RecyclerView.onMoreItems(onLoadMoreListener: OnLoadMoreListener?) {
    val adapter = (adapter as? CharactersAdapter)
    adapter?.let {
        addOnScrolledToEnd { offset ->
            onLoadMoreListener?.onLoadMore(offset)
        }
    }
}

interface OnLoadMoreListener {
    fun onLoadMore(offset: Int)
}
