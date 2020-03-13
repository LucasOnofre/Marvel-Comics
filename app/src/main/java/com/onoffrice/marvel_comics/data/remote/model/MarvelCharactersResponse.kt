package com.onoffrice.marvel_comics.data.remote.model

import com.google.gson.annotations.SerializedName


data class MarvelCharactersResponse(

    @SerializedName("data")
    val charactersData: CharactersData
)

data class CharactersData(
    val count: Int,
    val limit: Int,
    val offset: Int,
    val charactersInfo: List<CharactersInfo>,
    val total: Int
)

data class CharactersInfo(
    val comics: Comics,
    val description: String,
    val events: Events,
    val id: Int,
    val modified: String,
    val name: String,
    val resourceURI: String,
    val series: Series,
    val stories: Stories,
    val thumbnail: Thumbnail,
    val urls: List<Url>
)

data class Comics(
    val available: Int,
    val collectionURI: String,
    val items: List<Item>,
    val returned: Int
)

data class Item(
    val name: String,
    val resourceURI: String
)

data class Events(
    val available: Int,
    val collectionURI: String,
    val items: List<Any>,
    val returned: Int
)

data class Series(
    val available: Int,
    val collectionURI: String,
    val items: List<ItemX>,
    val returned: Int
)

data class ItemX(
    val name: String,
    val resourceURI: String
)

data class Stories(
    val available: Int,
    val collectionURI: String,
    val items: List<ItemXX>,
    val returned: Int
)

data class ItemXX(
    val name: String,
    val resourceURI: String,
    val type: String
)

data class Thumbnail(
    val extension: String,
    val path: String
)

data class Url(
    val type: String,
    val url: String
)