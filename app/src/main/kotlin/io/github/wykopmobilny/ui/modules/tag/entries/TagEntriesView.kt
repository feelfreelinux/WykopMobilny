package io.github.wykopmobilny.ui.modules.tag.entries

import io.github.wykopmobilny.api.responses.TagMetaResponse
import io.github.wykopmobilny.ui.fragments.entries.EntriesFragmentView

interface TagEntriesView : EntriesFragmentView {
    fun setParentMeta(tagMeta: TagMetaResponse)
}
