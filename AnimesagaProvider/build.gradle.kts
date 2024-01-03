// use an integer for version numbers
version = 3


cloudstream {
    language = "hi"
    // All of these properties are optional, you can safely remove them

    description = "Animesaga has vast library of hindi dubbed anime/movies."
    authors = listOf("lirena")

    /**
     * Status int as the following:
     * 0: Down
     * 1: Ok
     * 2: Slow
     * 3: Beta only
     * */
    status = 1 // will be 3 if unspecified
    tvTypes = listOf(
        "Anime",
        "AnimeMovie",
        "Cartoon",
        "TvSeries"
    )

    iconUrl = "https://www.google.com/s2/favicons?sz=256&domain=https://www.animesaga.in/"
}
