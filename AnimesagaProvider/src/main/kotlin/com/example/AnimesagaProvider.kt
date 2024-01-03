package com.lirena
import com.fasterxml.jackson.annotation.JsonProperty
import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.AppUtils.parseJson
import com.lagradost.cloudstream3.utils.ExtractorLink
import com.lagradost.cloudstream3.utils.M3u8Helper
import com.lagradost.cloudstream3.AcraApplication.Companion.context
import com.lagradost.nicehttp.requestCreator
import java.net.Authenticator
import java.net.InetSocketAddress
import java.net.PasswordAuthentication
import java.net.Proxy


class AnimesagaProvider : MainAPI() { // all providers must be an instance of MainAPI
    override var mainUrl = "https://www.animesaga.in/" 
    val apiurl = "https://teplix.vercel.app/animesaga"
    override var name = "Animesaga"
    override val supportedTypes = setOf(
        TvType.Movie,
        TvType.Anime,
        TvType.Cartoon,
        TvType.AnimeMovie
        )

    override var lang = "en"

    // enable this when your provider has a main page
    override val hasMainPage = true
    

    data class Crunchyroll (

    @JsonProperty("img"   ) var img   : String? = null,
    @JsonProperty("title" ) var title : String? = null,
    @JsonProperty("link"  ) var link  : String? = null

    )

    data class Shows (

    @JsonProperty("img"   ) var img   : String? = null,
    @JsonProperty("title" ) var title : String? = null,
    @JsonProperty("link"  ) var link  : String? = null

    )

    data class Items (

    @JsonProperty("title" ) var title : String? = null,
    @JsonProperty("link"  ) var link  : String? = null,
    @JsonProperty("img"   ) var img   : String? = null

    )

    data class Recent (

    @JsonProperty("img"           ) var img           : String? = null,
    @JsonProperty("title"         ) var title         : String? = null,
    @JsonProperty("episode-title" ) var episodeTitle : String? = null,
    @JsonProperty("link"          ) var link          : String? = null,
    @JsonProperty("episode_no"    ) var episodeNo     : Int?    = null

    )
    data class Movies (

    @JsonProperty("img"   ) var img   : String? = null,
    @JsonProperty("title" ) var title : String? = null,
    @JsonProperty("link"  ) var link  : String? = null

    )

    data class Info (

    @JsonProperty("title"   ) var title   : String?            = null,
    @JsonProperty("tags"    ) var tags    : ArrayList<String>  = arrayListOf(),
    @JsonProperty("seasons" ) var seasons : ArrayList<Seasons> = arrayListOf()

    )

    data class Seasons (
    
    @JsonProperty("episodes") var episodes : ArrayList<Episodes> =arrayListOf()

    )
    data class Episodes (

    @JsonProperty("image" ) var image : String? = null,
    @JsonProperty("title" ) var title : String? = null,
    @JsonProperty("link"  ) var link  : String? = null

    )

    data class EpisodeLink (

    @JsonProperty("token"    ) var token    : String? = null,
    @JsonProperty("file"     ) var file     : String? = null,
    @JsonProperty("subtitle" ) var subtitle : String? = null

    )
 

/* 
    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse {
    val items = ArrayList<HomePageList>()
    
    // Fetch Recent items
    val aaaa = app.get("$apiurl/recent").parsed<List<Recent>>()
    val epss = aaaa.map { recent ->
        recent.toHome() // Assuming toHome is a function defined somewhere
    }
    
    // Fetch Crunchyroll items
    val res = app.get("$apiurl/crunchyroll").parsed<List<Crunchyroll>>() // Assuming Crunchyroll is another data class you've defined
    val home = res.map { item ->
        val title = item.title
        val poster = item.img
        val seriesID = item.link
        newAnimeSearchResponse(title!!,seriesID) {
            this.posterUrl = poster
        }
    }
    
    // Add items to the list
    if (home.isNotEmpty()) items.add(HomePageList("Popular", home))
    if (epss.isNotEmpty()) items.add(HomePageList("New episodes (SUB)", epss, true))
    // Add other items as needed
    
    // Throw an exception if no items are added
    if (items.isEmpty()) throw ErrorLoadingException()
    
    return HomePageResponse(items)
}

*/





    data class AnimeItem(
        @JsonProperty("title") var title: String? = null,
        @JsonProperty("link") var link: String? = null,
        @JsonProperty("img") var img: String? = null
    )

    data class AnimeItems(
        @JsonProperty("items") val items: List<AnimeItem>
    )
    
    data class CustomData(
        @JsonProperty("seriesID") val seriesID: String?
    )




    // this function gets called when you search for something
    override suspend fun search(query: String): List<SearchResponse> {
        val url = "$apiurl/animesaga/search?query=$query"
        val res = app.get(url).parsed<AnimeItems>()

        val search = res.items.map { item ->
            val title = item.title
            val image = item.img
            val id = item.link
            val data = CustomData(id).toJson()

            newAnimeSearchResponse(title!!, data) {
                this.posterUrl = image
            }
        }

        return search // Return the populated list
    }

}