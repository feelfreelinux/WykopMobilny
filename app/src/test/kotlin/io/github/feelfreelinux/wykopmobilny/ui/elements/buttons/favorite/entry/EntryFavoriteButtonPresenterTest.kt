package io.github.feelfreelinux.wykopmobilny.ui.elements.buttons.favorite.entry

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import io.github.feelfreelinux.wykopmobilny.api.entries.EntriesApi
import io.github.feelfreelinux.wykopmobilny.models.pojo.entries.FavoriteEntryResponse
import io.github.feelfreelinux.wykopmobilny.ui.elements.buttons.favorite.FavoriteButtonView
import io.github.feelfreelinux.wykopmobilny.utils.rx.SubscriptionHelper
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.io.IOException

class EntryFavoriteButtonPresenterTest {
    lateinit var systemUnderTest : EntryFavoriteButtonPresenter
    val mockOfEntriesApi = mock<EntriesApi>()
    val subscriptionHelper = SubscriptionHelper(Schedulers.trampoline(), Schedulers.trampoline())
    val mockOfView = mock<EntryFavoriteButtonView>()

    @Before
    fun setup() {
        systemUnderTest = EntryFavoriteButtonPresenter(subscriptionHelper, mockOfEntriesApi)
        systemUnderTest.subscribe(mockOfView)
    }

    @Test
    fun marksEntryFavorite() {
        val response = FavoriteEntryResponse(true)
        whenever(mockOfView.entryId).thenReturn(14)
        whenever(mockOfEntriesApi.markFavorite(any())).thenReturn(Single.just(response))

        systemUnderTest.markFavorite()
        verify(mockOfView).isFavorite = response.userFavorite
    }

    @Test
    fun unmarksEntryFavorite() {
        val response = FavoriteEntryResponse(false)
        whenever(mockOfView.entryId).thenReturn(12)
        whenever(mockOfEntriesApi.markFavorite(any())).thenReturn(Single.just(response))

        systemUnderTest.markFavorite()
        verify(mockOfView).isFavorite = response.userFavorite
    }

    @Test
    fun showsErrorDialog() {
        whenever(mockOfView.entryId).thenReturn(11)
        whenever(mockOfEntriesApi.markFavorite(any())).thenReturn(Single.error(IOException()))

        systemUnderTest.markFavorite()
        verify(mockOfView).showErrorDialog(any())
    }


}