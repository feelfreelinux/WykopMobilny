package io.github.feelfreelinux.wykopmobilny.ui.mikroblog.entry

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import com.squareup.moshi.Moshi
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Entry
import io.github.feelfreelinux.wykopmobilny.api.entries.EntriesApi
import io.github.feelfreelinux.wykopmobilny.ui.SubscribeHelperTest
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import java.io.IOException

class EntryPresenterTest {
    lateinit var systemUnderTest: EntryPresenter
    val moshi = Moshi.Builder().build()
    val mockOfView = mock<EntryView>()
    val mockOfEntriesApi = mock<EntriesApi>()

    @Before
    fun setup() {
        systemUnderTest = EntryPresenter(SubscribeHelperTest(), mockOfEntriesApi)
        systemUnderTest.subscribe(mockOfView)
    }

    @Test
    fun testSuccess() {
        val testEntry =
                mock<Entry>()

        systemUnderTest.entryId = 12

        whenever(mockOfEntriesApi.getEntryIndex(any())).thenReturn(Single.just(testEntry))

        systemUnderTest.loadData()
        verify(mockOfView).showEntry(any())
    }

    @Test
    fun testFailure() {
        systemUnderTest.entryId = 12

        whenever(mockOfEntriesApi.getEntryIndex(any())).thenReturn(Single.error(IOException()))

        systemUnderTest.loadData()
        verify(mockOfView).showErrorDialog(any())
    }

}