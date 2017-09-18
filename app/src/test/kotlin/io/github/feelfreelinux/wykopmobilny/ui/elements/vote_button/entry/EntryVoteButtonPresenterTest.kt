package io.github.feelfreelinux.wykopmobilny.ui.elements.vote_button.entry

import com.nhaarman.mockito_kotlin.*
import com.squareup.moshi.Moshi
import io.github.feelfreelinux.wykopmobilny.api.Profile
import io.github.feelfreelinux.wykopmobilny.api.VoteResponse
import io.github.feelfreelinux.wykopmobilny.api.entries.EntriesApi
import io.github.feelfreelinux.wykopmobilny.api.user.UserApi
import io.github.feelfreelinux.wykopmobilny.ui.splashscreen.SplashScreenPresenter
import org.junit.Before
import org.junit.Test
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class EntryVoteButtonPresenterTest {
    lateinit var systemUnderTest: EntryVoteButtonPresenter
    val mockOfView = mock<EntryVoteButtonView>()
    val mockOfEntriesApi = mock<EntriesApi>()

    @Before
    fun setup() {
        systemUnderTest = EntryVoteButtonPresenter(mockOfEntriesApi)
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
        whenever(mockOfEntriesApi.voteEntry(any())).thenReturn(mockOfCall)
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
        whenever(mockOfEntriesApi.unvoteEntry(any())).thenReturn(mockOfCall)
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
        whenever(mockOfEntriesApi.voteEntry(any())).thenReturn(mockOfCall)
        whenever(mockOfEntriesApi.unvoteEntry(any())).thenReturn(mockOfCall)

        systemUnderTest.vote()
        systemUnderTest.unvote()

        verify(mockOfView, times(2)).showErrorDialog(any())
    }
}