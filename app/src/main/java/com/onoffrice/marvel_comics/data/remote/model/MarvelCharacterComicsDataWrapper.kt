package com.onoffrice.marvel_comics.data.remote.model

import android.provider.CalendarContract
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class MarvelCharacterComicsDataWrapper(

    @SerializedName("data")
    val characterComicsData: CharacterComicsDataContainer
):Serializable

data class CharacterComicsDataContainer (
    val count: Int,
    val limit: Int,
    val offset: Int,
    @SerializedName("results")
    val comics: List<ComicModel>,
    val total: Int
):Serializable

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
    val events: Event,
    val format: String,
    val id: Int,
    val images: List<Image>,
    val isbn: String,
    val issn: String,
    val issueNumber: Int,
    val modified: String,
    val pageCount: Int,
    val prices: ArrayList<Price>,
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
):Serializable

data class Characters(
    val available: Int,
    val collectionURI: String,
    @SerializedName("items")
    val characterSummary: List<CharacterSummary>,
    val returned: Int
):Serializable

data class CharacterSummary(
    val name: String,
    val resourceURI: String
):Serializable

data class Creators(
    val available: Int,
    val collectionURI: String,
    @SerializedName("items")
    val items: List<CreatorSummary>,
    val returned: Int
):Serializable

data class CreatorSummary (
    val name: String,
    val resourceURI: String,
    val role: String
):Serializable


data class Date(
    val date: String,
    val type: String
):Serializable

data class Image(
    val extension: String,
    val path: String
):Serializable

data class Price(
    val price: Double,
    val type: String
):Serializable

data class Series(
    val name: String,
    val resourceURI: String
):Serializable

data class TextObject(
    val language: String,
    val text: String,
    val type: String
):Serializable

data class Variant(
    val name: String,
    val resourceURI: String
):Serializable