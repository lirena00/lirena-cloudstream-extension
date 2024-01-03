package com.lirena
import com.fasterxml.jackson.annotation.JsonProperty
import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.AppUtils.parseJson
import com.lagradost.cloudstream3.utils.ExtractorLink
import com.lagradost.cloudstream3.utils.M3u8Helper
import com.lagradost.cloudstream3.utils.AppUtils.toJson
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

    data class RecentItems(
        @JsonProperty("recent") val items: List<RecentItem>
    )

    data class RecentItem(
        @JsonProperty("title") var title: String? = null,
        @JsonProperty("link") var link: String? = null,
        @JsonProperty("img") var img: String? = null,
        @JsonProperty("episode-title") var episode_title: String? = null,
        @JsonProperty("episode_no") var episode_no: String? = null,
    )

    data class CrunchyItem(
        @JsonProperty("title") var title: String? = null,
        @JsonProperty("link") var link: String? = null,
        @JsonProperty("img") var img: String? = null
    )

    data class CrunchyItems(
        @JsonProperty("crunchyroll") val items: List<CrunchyItem>
    )

    data class ShowItem(
        @JsonProperty("title") var title: String? = null,
        @JsonProperty("link") var link: String? = null,
        @JsonProperty("img") var img: String? = null
    )

    data class ShowItems(
        @JsonProperty("shows") val items: List<ShowItem>
    )

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

    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse {
        val homes = ArrayList<HomePageList>()

        val url = "$apiurl/recent"
        val recentRes = app.get(url).parsed<RecentItems>()

        val recent = recentRes.items.map { item ->
            val title = item.title
            val poster = item.img
            val id = item.link
            val data = CustomData(id).toJson()

            newAnimeSearchResponse(title!!, data) {
                this.posterUrl = poster
                isHorizontal=true
            }
        }

        val url_2 = "$apiurl/crunchyroll"
        val crunchyRes = app.get(url_2).parsed<CrunchyItems>()

        val crunchy = crunchyRes.items.map { item ->
            val title = item.title
            val image = item.img
            val id = item.link
            val data = CustomData(id).toJson()

            newAnimeSearchResponse(title!!, data) {
                this.posterUrl = image
            }
        }

        val url_3 = "$apiurl/shows"
        val showRes = app.get(url_3).parsed<ShowItems>()

        val show = showRes.items.map { item ->
            val title = item.title
            val image = item.img
            val id = item.link
            val data = CustomData(id).toJson()

            newAnimeSearchResponse(title!!, data) {
                this.posterUrl = image
            }
        }

        homes.add(HomePageList("Recent", recent))
        homes.add(HomePageList("Crunchyroll Dubs", crunchy))
        homes.add(HomePageList("Shows", show))
        return HomePageResponse(homes) // Return the populated list
    }

    // this function gets called when you search for something
    override suspend fun search(query: String): List<SearchResponse> {
        val url = "$apiurl/search?query=$query"
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