package com.onoffrice.marvel_comics.data.remote.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class CharacterDataWrapper(

    @SerializedName("data")
    val charactersData: CharacterDataContainer
):Serializable

data class CharacterDataContainer(
    val count: Int,
    val limit: Int,
    val offset: Int,
    @SerializedName("results")
    val characters: List<Character>,
    val total: Int
):Serializable

data class Character(
    val comics: Comic,
    val description: String,
    val events: Event,
    val id: Int,
    val modified: String,
    val name: String,
    val resourceURI: String,
    val series: Serie,
    val stories: Story,
    val thumbnail: Thumbnail,
    val urls: List<Url>
):Serializable

data class Comic(
    val available: Int,
    val collectionURI: String,
    @SerializedName("items")
    val comicSummary: List<ComicSummary>,
    val returned: Int
):Serializable

data class ComicSummary(
    val name: String,
    val resourceURI: String
):Serializable

data class Event(
    val available: Int,
    val collectionURI: String,
    @SerializedName("items")
    val eventSummary: List<EventSummary>,
    val returned: Int
):Serializable

data class EventSummary(
    val name: String,
    val resourceURI: String
):Serializable

data class Serie(
    val available: Int,
    val collectionURI: String,
    @SerializedName("items")
    val serieSummary: List<SerieSummary>,
    val returned: Int
):Serializable

data class SerieSummary(
    val name: String,
    val resourceURI: String
):Serializable

data class Story(
    val available: Int,
    val collectionURI: String,
    @SerializedName("items")
    val storySummary: List<StorySummary>,
    val returned: Int
):Serializable

data class StorySummary(
    val name: String,
    val resourceURI: String,
    val type: String
):Serializable

data class Thumbnail(
    val extension: String,
    val path: String

):Serializable {
    fun getPathExtension(): String {
        return "$path.$extension"
    }
}

data class Url(
    val type: String,
    val url: String
):Serializable
