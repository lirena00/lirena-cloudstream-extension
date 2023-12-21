// use an integer for version numbers
version = 32


cloudstream {
    language = "hi"
    // All of these properties are optional, you can safely remove them

    description = "With this extension you will be able to watch hindi hardsubbed/dubbed anime available in the Indian catalog of Crunchyroll."
    authors = listOf("Stormunblessed, lirena")

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
        "OVA",
    )

    iconUrl = "https://raw.githubusercontent.com/Stormunblessed/IPTV-CR-NIC/main/logos/kronch.png"
}
