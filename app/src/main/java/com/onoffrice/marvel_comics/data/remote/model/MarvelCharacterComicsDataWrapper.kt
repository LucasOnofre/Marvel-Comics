package com.onoffrice.marvel_comics.data.remote.model

import android.provider.CalendarContract
import com.google.gson.annotations.SerializedName

data class MarvelCharacterComicsDataWrapper(

    @SerializedName("data")
    val characterComicsData: CharacterComicsDataContainer
)

data class CharacterComicsDataContainer (
    val count: Int,
    val limit: Int,
    val offset: Int,
    @SerializedName("results")
    val comics: List<ComicModel>,
    val total: Int
)

data class ComicModel(
    val characters: Characters,
    val collectedIssues: List<Any>,
    val collections: List<Any>,
    val creators: Creators,
    val dates: List<Date>,
    val description: String,
    val diamondCode: String,
    val digitalId: Int,
    val ean: String,
    val events: CalendarContract.Events,
    val format: String,
    val id: Int,
    val images: List<Image>,
    val isbn: String,
    val issn: String,
    val issueNumber: Int,
    val modified: String,
    val pageCount: Int,
    val prices: List<Price>,
    val resourceURI: String,
    val series: Series,
    val stories: StorySummary,
    val textObjects: List<TextObject>,
    val thumbnail: Thumbnail,
    val title: String,
    val upc: String,
    val urls: List<Url>,
    val variantDescription: String,
    val variants: List<Variant>
)

data class Characters(
    val available: Int,
    val collectionURI: String,
    val items: List<Item>,
    val returned: Int
)

data class Item(
    val name: String,
    val resourceURI: String
)

data class Creators(
    val available: Int,
    val collectionURI: String,
    @SerializedName("items")
    val items: List<CreatorSummary>,
    val returned: Int
)

data class CreatorSummary (
    val name: String,
    val resourceURI: String,
    val role: String
)

data class Date(
    val date: String,
    val type: String
)

data class Image(
    val extension: String,
    val path: String
)

data class Price(
    val price: Double,
    val type: String
)

data class Series(
    val name: String,
    val resourceURI: String
)


data class TextObject(
    val language: String,
    val text: String,
    val type: String
)

data class Variant(
    val name: String,
    val resourceURI: String
)