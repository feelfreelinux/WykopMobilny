package io.github.feelfreelinux.wykopmobilny.ui.elements.vote_button.entry.comment

import com.nhaarman.mockito_kotlin.*
import io.github.feelfreelinux.wykopmobilny.api.VoteResponse
import io.github.feelfreelinux.wykopmobilny.api.entries.EntriesApi
import org.junit.Before
import org.junit.Test
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class EntryCommentVoteButtonPresenterTest {
    lateinit var systemUnderTest: EntryCommentVoteButtonPresenter
    val mockOfView = mock<EntryCommentVoteButtonView>()
    val mockOfEntriesApi = mock<EntriesApi>()

    @Before
    fun setup() {
        systemUnderTest = EntryCommentVoteButtonPresenter(mockOfEntriesApi)
        systemUnderTest.subscribe(mockOfView)
    }

    @Test
    fun shouldUpdateButtonOnVoteResponse() {
        val EXPECTED_VOTES = 211
        val response = Response.success(VoteResponse(EXPECTED_VOTES, emptyList()))
        val mockOfCall = mock<Call<VoteResponse>>()

        systemUnderTest.entryId = 12

        whenever(mockOfCall.enqueue(any())).thenAnswer {
            (it.arguments.first() as Callback<VoteResponse>).onResponse(null, response)
        }
        whenever(mockOfEntriesApi.voteComment(any(), any())).thenReturn(mockOfCall)
        systemUnderTest.vote()

        verify(mockOfView).apply {
            voteCount = EXPECTED_VOTES
            isButtonSelected = true
        }
    }

    @Test
    fun shouldUpdateButtonOnUnVoteResponse() {
        val EXPECTED_VOTES = 115
        val response = Response.success(VoteResponse(EXPECTED_VOTES, emptyList()))
        val mockOfCall = mock<Call<VoteResponse>>()

        systemUnderTest.entryId = 12

        whenever(mockOfCall.enqueue(any())).thenAnswer {
            (it.arguments.first() as Callback<VoteResponse>).onResponse(null, response)
        }
        whenever(mockOfEntriesApi.unvoteComment(any(), any())).thenReturn(mockOfCall)
        systemUnderTest.unvote()

        verify(mockOfView).apply {
            voteCount = EXPECTED_VOTES
            isButtonSelected = false
        }
    }

    @Test
    fun shouldShowErrorDialog() {
        val mockOfCall = mock<Call<VoteResponse>>()

        systemUnderTest.entryId = 12

        whenever(mockOfCall.enqueue(any())).thenAnswer {
            (it.arguments.first() as Callback<VoteResponse>).onFailure(null, IOException())
        }
        whenever(mockOfEntriesApi.voteComment(any(), any())).thenReturn(mockOfCall)
        whenever(mockOfEntriesApi.unvoteComment(any(), any())).thenReturn(mockOfCall)

        systemUnderTest.vote()
        systemUnderTest.unvote()

        verify(mockOfView, times(2)).showErrorDialog(any())
    }
}