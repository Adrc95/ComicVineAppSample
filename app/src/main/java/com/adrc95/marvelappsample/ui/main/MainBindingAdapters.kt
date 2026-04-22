package com.adrc95.marvelappsample.ui.main

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.adrc95.domain.Character
import com.adrc95.marvelappsample.ui.common.addOnScrolledToEnd
import com.adrc95.marvelappsample.ui.favorite.FavoriteCharactersAdapter

@BindingAdapter(value = ["items", "isMoreItemsLoad"], requireAll = false)
fun RecyclerView.setItems(characters: List<Character>?, isMoreItemLoad: Boolean?) {
    val items = characters ?: return
    when (val currentAdapter = adapter) {
        is CharactersAdapter -> {
            if (!currentAdapter.isApplicatedFilter) {
                if (currentAdapter.characters.isNullOrEmpty()) {
                    currentAdapter.characters = items
                    currentAdapter.submitList(currentAdapter.characters)
                } else if (isMoreItemLoad == true) {
                    currentAdapter.characters = currentAdapter.characters!! + items
                    currentAdapter.submitList(currentAdapter.characters)
                }
            }
            currentAdapter.isApplicatedFilter = false
        }
        is FavoriteCharactersAdapter -> currentAdapter.submitList(items)
        else -> Unit
    }
}

@BindingAdapter("filter")
fun RecyclerView.setFilter(text: String?) {
    val adapter = (adapter as? CharactersAdapter)
    adapter?.let {
        text?.let {
            adapter.isApplicatedFilter = true
            adapter.filter.filter(text)
        }
    }
}

@BindingAdapter("onMoreItems")
fun RecyclerView.onMoreItems(onLoadMoreListener: OnLoadMoreListener?) {
    val adapter = (adapter as? CharactersAdapter)
    adapter?.let {
        addOnScrolledToEnd { totalItemCount ->
            if (!it.isApplicatedFilter) {
                adapter.isLoadMoreCharacters = true
                onLoadMoreListener?.onLoadMore(totalItemCount)
            }
        }
    }
}

interface OnLoadMoreListener {
    fun onLoadMore(totalItemCount: Int)
}
