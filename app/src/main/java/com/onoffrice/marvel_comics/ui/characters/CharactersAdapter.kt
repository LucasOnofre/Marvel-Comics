package com.onoffrice.marvel_comics.ui.characters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.onoffrice.marvel_comics.R
import com.onoffrice.marvel_comics.data.remote.model.Character
import com.onoffrice.marvel_comics.utils.extensions.loadImage
import kotlinx.android.synthetic.main.adapter_character_item.view.*

class CharactersAdapter (private val listener: CharacterClickListener?): RecyclerView.Adapter<CharactersAdapter.GameViewHolderItem>() {

    interface CharacterClickListener {
        fun onClickCharacter(character: Character)
    }

    var list: MutableList<Character> = mutableListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    fun setCharacters(characters: List<Character>) {
        list.addAll(characters)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): GameViewHolderItem {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.adapter_character_item, parent, false)

        return GameViewHolderItem(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: GameViewHolderItem, position: Int) {
        val characterItem = list[position]

        characterItem.let {
            holder.title.text = characterItem.name
            holder.poster.loadImage(characterItem.thumbnail.getPathExtension())

            // Character click listener
            holder.itemView.setOnClickListener {
                listener?.onClickCharacter(characterItem)
            }
        }
    }

    fun resetList() {
        list.clear()
        notifyDataSetChanged()
    }

    class GameViewHolderItem(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val poster= itemView.poster
        val title  = itemView.title
    }
}
