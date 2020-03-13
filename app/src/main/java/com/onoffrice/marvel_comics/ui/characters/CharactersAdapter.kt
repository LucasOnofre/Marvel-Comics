package com.onoffrice.marvel_comics.ui.characters

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.onoffrice.marvel_comics.R


interface GameClickListener {
    fun onClickCharacter(character: MarvelCharacter)
}

class GamesAdapter (
    private val context: Activity,
    private val listener: GameClickListener

): RecyclerView.Adapter<GamesAdapter.GameViewHolderItem>() {

    var list: MutableList<MarvelCharacter> = mutableListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): GameViewHolderItem {

        val view: View =
            LayoutInflater.from(context).inflate(R.layout.adapter_movie_item, parent, false)

        return GameViewHolderItem(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: GameViewHolderItem, position: Int) {

        val topItem = list[position]

        topItem.let {
            setPosterPath(topItem, holder)
            holder.title.text = topItem.game?.name

            // Game Click listener
            holder.itemView.setOnClickListener {
                listener.onClickGame(topItem)
            }
        }
    }

    /** Loads the game image using Picasso **/
    private fun setPosterPath(topItem: Top, holder: GameViewHolderItem) {
        val url = topItem.game?.box?.large
        url?.loadPicasso(holder.poster)
    }


    class GameViewHolderItem(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val poster = itemView.poster!!
        val title = itemView.title!!
    }
}
