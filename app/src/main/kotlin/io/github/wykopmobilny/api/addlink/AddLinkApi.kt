package io.github.wykopmobilny.api.addlink

import io.github.wykopmobilny.api.responses.AddLinkPreviewImage
import io.github.wykopmobilny.api.responses.NewLinkResponse
import io.github.wykopmobilny.models.dataclass.Link
import io.reactivex.Single

interface AddLinkApi {
    fun getDraft(url: String): Single<NewLinkResponse>
    fun getImages(key: String): Single<List<AddLinkPreviewImage>>
    fun publishLink(
        key: String,
        title: String,
        description: String,
        tags: String,
        photo: String,
        url: String,
        plus18: Boolean
    ): Single<Link>
}
