package io.github.feelfreelinux.wykopmobilny.ui.widgets.buttons.vote.entry.comment

import com.nhaarman.mockito_kotlin.*
import io.github.feelfreelinux.wykopmobilny.TestSubscriptionHelper
import io.github.feelfreelinux.wykopmobilny.api.entries.EntriesApi
import io.github.feelfreelinux.wykopmobilny.models.pojo.apiv2.models.VoteResponse
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import java.io.IOException

class EntryCommentVoteButtonPresenterTest {
    lateinit var systemUnderTest: EntryCommentVoteButtonPresenter
    val mockOfView = mock<EntryCommentVoteButtonView>()
    val mockOfEntriesApi = mock<EntriesApi>()
    val subscribeHelper = TestSubscriptionHelper()

    @Before
    fun setup() {
        systemUnderTest = EntryCommentVoteButtonPresenter(subscribeHelper, mockOfEntriesApi)
        systemUnderTest.subscribe(mockOfView)
    }

    @Test
    fun shouldUpdateButtonOnVoteResponse() {
        val EXPECTED_VOTES = 211

        whenever(mockOfEntriesApi.voteComment(any())).thenReturn(Single.just(VoteResponse(EXPECTED_VOTES)))
        systemUnderTest.vote()

        verify(mockOfView).apply {
            voteCount = EXPECTED_VOTES
            isButtonSelected = true
        }
    }

    @Test
    fun shouldUpdateButtonOnUnVoteResponse() {
        val EXPECTED_VOTES = 115


        whenever(mockOfEntriesApi.unvoteComment(any())).thenReturn(Single.just(VoteResponse(EXPECTED_VOTES)))
        systemUnderTest.unvote()

        verify(mockOfView).apply {
            voteCount = EXPECTED_VOTES
            isButtonSelected = false
        }
    }

    @Test
    fun shouldShowErrorDialog() {
        whenever(mockOfEntriesApi.voteComment(any())).thenReturn(Single.error(IOException()))
        whenever(mockOfEntriesApi.unvoteComment(any())).thenReturn(Single.error(IOException()))

        systemUnderTest.vote()
        systemUnderTest.unvote()

        verify(mockOfView, times(2)).showErrorDialog(any())
    }
}