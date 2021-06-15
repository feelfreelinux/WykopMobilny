package io.github.wykopmobilny

import io.github.wykopmobilny.blacklist.remote.DaggerScraperComponent
import io.github.wykopmobilny.blacklist.remote.ScraperComponent
import io.github.wykopmobilny.domain.DaggerDomainComponent
import io.github.wykopmobilny.domain.DomainComponent
import io.github.wykopmobilny.patrons.remote.DaggerPatronsComponent
import io.github.wykopmobilny.patrons.remote.PatronsComponent
import io.github.wykopmobilny.wykop.remote.DaggerWykopComponent
import io.github.wykopmobilny.wykop.remote.WykopComponent

fun daggerScraper(): ScraperComponent.Factory = DaggerScraperComponent.factory()
fun daggerWykop(): WykopComponent.Factory = DaggerWykopComponent.factory()
fun daggerPatrons(): PatronsComponent.Factory = DaggerPatronsComponent.factory()
fun daggerDomain(): DomainComponent.Factory = DaggerDomainComponent.factory()
