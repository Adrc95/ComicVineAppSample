package com.adrc95.comicvineappsample.ui.main.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.adrc95.comicvineappsample.R
import com.adrc95.comicvineappsample.databinding.ViewCharacterBinding
import com.adrc95.comicvineappsample.ui.common.basicDiffUtil
import com.adrc95.comicvineappsample.ui.common.inflate
import com.adrc95.comicvineappsample.ui.model.CharacterDisplayModel

class CharactersAdapter(private val listener: (CharacterDisplayModel) -> Unit) :
    ListAdapter<CharacterDisplayModel, CharactersAdapter.ViewHolder>(basicDiffUtil { old, new -> old.id == new.id }) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = parent.inflate(R.layout.view_character, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val character = getItem(position)
        holder.bind(character)
        holder.itemView.setOnClickListener { listener(character) }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ViewCharacterBinding.bind(view)
        fun bind(character: CharacterDisplayModel) {
            binding.character = character
        }
    }
}
