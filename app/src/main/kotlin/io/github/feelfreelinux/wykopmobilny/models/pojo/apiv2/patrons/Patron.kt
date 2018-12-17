package io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.patrons

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty


@JsonIgnoreProperties(ignoreUnknown = true)
data class Patron(
        @JsonProperty("listMention") val listMention: Boolean,
        @JsonProperty("tier") val tier: String,
        @JsonProperty("username") val username: String,
        @JsonProperty("badge") val badge: PatronBadge)