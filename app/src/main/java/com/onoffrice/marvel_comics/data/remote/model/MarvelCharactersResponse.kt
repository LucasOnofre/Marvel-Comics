package com.onoffrice.marvel_comics.data.remote.model

import com.google.gson.annotations.SerializedName


data class CharacterDataWrapper(

    @SerializedName("data")
    val charactersData: CharacterDataContainer
)

data class CharacterDataContainer(
    val count: Int,
    val limit: Int,
    val offset: Int,
    @SerializedName("results")
    val characters: List<Character>,
    val total: Int
)

data class Character(
    val comics: Comic,
    val description: String,
    val events: Event,
    val id: Int,
    val modified: String,
    val name: String,
    val resourceURI: String,
    val series: Serie,
    val stories: Storie,
    val thumbnail: Thumbnail,
    val urls: List<Url>
)

data class Comic(
    val available: Int,
    val collectionURI: String,
    val items: List<Item>,
    val returned: Int
)

data class Item(
    val name: String,
    val resourceURI: String
)

data class Event(
    val available: Int,
    val collectionURI: String,
    val eventSummary: List<EventSummary>,
    val returned: Int
)

data class EventSummary(
    val name: String,
    val resourceURI: String
)

data class Serie(
    val available: Int,
    val collectionURI: String,
    val serieSummary: List<SerieSummary>,
    val returned: Int
)

data class SerieSummary(
    val name: String,
    val resourceURI: String
)

data class Storie(
    val available: Int,
    val collectionURI: String,
    val storySummary: List<StorySummary>,
    val returned: Int
)

data class StorySummary(
    val name: String,
    val resourceURI: String,
    val type: String
)

data class Thumbnail(
    val extension: String,
    val path: String

)  {
    fun getPathExtension(): String {
    return path + "." + extension
    }
}

data class Url(
    val type: String,
    val url: String
)
