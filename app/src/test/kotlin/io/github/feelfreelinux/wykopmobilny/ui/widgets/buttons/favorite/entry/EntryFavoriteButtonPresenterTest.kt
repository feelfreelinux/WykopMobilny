package io.github.feelfreelinux.wykopmobilny.ui.widgets.buttons.favorite.entry

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import io.github.feelfreelinux.wykopmobilny.TestSubscriptionHelper
import io.github.feelfreelinux.wykopmobilny.api.entries.EntriesApi
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.FavoriteResponse
import io.reactivex.Single
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.io.IOException

class EntryFavoriteButtonPresenterTest {
    lateinit var systemUnderTest : EntryFavoriteButtonPresenter
    val mockOfEntriesApi = mock<EntriesApi>()
    val subscribeHelper = TestSubscriptionHelper()
    val mockOfView = mock<EntryFavoriteButtonView>()

    @Before
    fun setup() {
        systemUnderTest = EntryFavoriteButtonPresenter(subscribeHelper, mockOfEntriesApi)
        systemUnderTest.subscribe(mockOfView)
    }

    @Test
    fun marksEntryFavorite() {
        val response = FavoriteResponse(true)
        assertEquals(-1, systemUnderTest.entryId)
        whenever(mockOfEntriesApi.markFavorite(any())).thenReturn(Single.just(response))

        systemUnderTest.markFavorite()
        verify(mockOfView).isFavorite = response.userFavorite
    }

    @Test
    fun unmarksEntryFavorite() {
        val response = FavoriteResponse(false)
        assertEquals(-1, systemUnderTest.entryId)
        whenever(mockOfEntriesApi.markFavorite(any())).thenReturn(Single.just(response))

        systemUnderTest.markFavorite()
        verify(mockOfView).isFavorite = response.userFavorite
    }

    @Test
    fun showsErrorDialog() {
        assertEquals(-1, systemUnderTest.entryId)
        whenever(mockOfEntriesApi.markFavorite(any())).thenReturn(Single.error(IOException()))

        systemUnderTest.markFavorite()
        verify(mockOfView).showErrorDialog(any())
    }


}