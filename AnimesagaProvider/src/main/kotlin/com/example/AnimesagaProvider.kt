package com.lirena
import com.fasterxml.jackson.annotation.JsonProperty
import com.lagradost.cloudstream3.TvType
import com.lagradost.cloudstream3.MainAPI
import com.lagradost.cloudstream3.SearchResponse

class AnimesagaProvider : MainAPI() { // all providers must be an instance of MainAPI
    override var mainUrl = "https://www.animesaga.in/animesaga" 
    const var apiurl = "https://teplix.vercel.app/"
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
 


    override suspend fun getMainPage(page: Int, request: MainPageRequest): HomePageResponse {
        val items = ArrayList<HomePageList>()
        val aaaa =
            app.get(
                "$apiurl/recent",
            ).parsed<Recent>()
        val epss =
            aaaa.items.map {
                    it.toHome(Recent)
                }
        val res =
            app.get(
                "$apiurl/crunchyroll",
            ).parsed<Crunchyroll>()
        val home =
            res.items.map {
                val title = it.title
                // val epss = it.seriesMetadata?.episodeCount
      //          val posterstring = it.img?.posterTall.toString()
                // val ttt = it.images?.posterTall?.get(0)?.get(6)?.source ?: ""
                val poster = it.img
                val seriesID = it.link
                newAnimeSearchResponse(title!!, data) {
                    this.posterUrl = poster
                    addDubStatus(isdub, issub)
                }
            }
        if (home.isNotEmpty()) items.add(HomePageList("Popular", home))
        if (epss.isNotEmpty()) items.add(HomePageList("New episodes (SUB)", epss, true))
        if (ssss.isNotEmpty()) items.add(HomePageList("New episodes (DUB)", ssss, true))
        if (items.size <= 0) throw ErrorLoadingException()
        return HomePageResponse(items)
}












    // this function gets called when you search for something
    override suspend fun search(query: String): List<SearchResponse> {
        return listOf<SearchResponse>()
    }
}
