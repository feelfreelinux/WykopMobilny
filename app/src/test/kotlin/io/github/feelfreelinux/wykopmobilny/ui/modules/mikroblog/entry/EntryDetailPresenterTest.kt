package io.github.feelfreelinux.wykopmobilny.ui.modules.mikroblog.entry

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import io.github.feelfreelinux.wykopmobilny.TestSubscriptionHelper
import io.github.feelfreelinux.wykopmobilny.models.dataclass.Entry
import io.github.feelfreelinux.wykopmobilny.api.entries.EntriesApi
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import java.io.IOException

class EntryDetailPresenterTest {
    lateinit var systemUnderTest: EntryDetailPresenter
    val mockOfView = mock<EntryDetailView>()
    val mockOfEntriesApi = mock<EntriesApi>()
    val subscribeHelper = TestSubscriptionHelper()

    @Before
    fun setup() {
        //systemUnderTest = EntryDetailPresenter(subscribeHelper, mockOfEntriesApi)
        systemUnderTest.subscribe(mockOfView)
    }

    @Test
    fun testSuccess() {
        val testEntry =
                mock<Entry>()

        systemUnderTest.entryId = 12

        whenever(mockOfEntriesApi.getEntry(any())).thenReturn(Single.just(testEntry))

        systemUnderTest.loadData()
        verify(mockOfView).showEntry(any())
    }

    @Test
    fun testFailure() {
        systemUnderTest.entryId = 12

        whenever(mockOfEntriesApi.getEntry(any())).thenReturn(Single.error(IOException()))

        systemUnderTest.loadData()
        verify(mockOfView).showErrorDialog(any())
    }

}