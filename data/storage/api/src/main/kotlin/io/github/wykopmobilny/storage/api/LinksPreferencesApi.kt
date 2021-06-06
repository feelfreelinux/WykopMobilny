package io.github.wykopmobilny.storage.api

interface LinksPreferencesApi {
    var readLinksIds: Set<String>?
    var linkCommentsDefaultSort: String?
    var upcomingDefaultSort: String?
}
