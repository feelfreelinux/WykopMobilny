package io.github.feelfreelinux.wykopmobilny.ui.mikroblog.entry

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import com.squareup.moshi.Moshi
import io.github.feelfreelinux.wykopmobilny.api.Entry
import io.github.feelfreelinux.wykopmobilny.api.entries.EntriesApi
import org.junit.Before
import org.junit.Test
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class EntryPresenterTest {
    lateinit var systemUnderTest: EntryPresenter
    val moshi = Moshi.Builder().build()
    val mockOfView = mock<EntryView>()
    val mockOfEntriesApi = mock<EntriesApi>()

    @Before
    fun setup() {
        systemUnderTest = EntryPresenter(mockOfEntriesApi)
        systemUnderTest.subscribe(mockOfView)
    }

    @Test
    fun testSuccess() {
        val mockOfCall = mock<Call<Entry>>()
        val entryAdapter = moshi.adapter(Entry::class.java)
        val testEntry = entryAdapter.fromJson("{}")
        val response = Response.success(testEntry)

        systemUnderTest.entryId = 12

        whenever(mockOfCall.enqueue(any())).thenAnswer {
            (it.arguments.first() as Callback<Entry>).onResponse(null, response)
        }

        whenever(mockOfEntriesApi.getEntryIndex(any())).thenReturn(mockOfCall)

        systemUnderTest.loadData()
        verify(mockOfView).showEntry(any())
    }

    @Test
    fun testFailure() {
        val mockOfCall = mock<Call<Entry>>()
        systemUnderTest.entryId = 12

        whenever(mockOfCall.enqueue(any())).thenAnswer {
            (it.arguments.first() as Callback<Entry>).onFailure(null, IOException())
        }
        whenever(mockOfEntriesApi.getEntryIndex(any())).thenReturn(mockOfCall)

        systemUnderTest.loadData()
        verify(mockOfView).showErrorDialog(any())
    }

}