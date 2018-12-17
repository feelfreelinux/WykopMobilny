package io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.patrons

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty


@JsonIgnoreProperties(ignoreUnknown = true)
data class PatronsResponse(@JsonProperty("patrons") val patrons: List<Patron>)