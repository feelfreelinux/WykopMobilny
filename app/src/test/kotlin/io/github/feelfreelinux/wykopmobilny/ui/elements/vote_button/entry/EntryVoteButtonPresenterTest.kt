package io.github.feelfreelinux.wykopmobilny.ui.elements.vote_button.entry

import com.nhaarman.mockito_kotlin.*
import io.github.feelfreelinux.wykopmobilny.models.pojo.VoteResponse
import io.github.feelfreelinux.wykopmobilny.api.entries.EntriesApi
import io.github.feelfreelinux.wykopmobilny.ui.SubscribeHelperTest
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import java.io.IOException

class EntryVoteButtonPresenterTest {
    lateinit var systemUnderTest: EntryVoteButtonPresenter
    val mockOfView = mock<EntryVoteButtonView>()
    val mockOfEntriesApi = mock<EntriesApi>()

    @Before
    fun setup() {
        systemUnderTest = EntryVoteButtonPresenter(SubscribeHelperTest(), mockOfEntriesApi)
        systemUnderTest.subscribe(mockOfView)
    }

    @Test
    fun shouldUpdateButtonOnVoteResponse() {
        val EXPECTED_VOTES = 211

        systemUnderTest.entryId = 12

        whenever(mockOfEntriesApi.voteEntry(any())).thenReturn(Single.just(VoteResponse(EXPECTED_VOTES, emptyList())))
        systemUnderTest.vote()

        verify(mockOfView).apply {
            voteCount = EXPECTED_VOTES
            isButtonSelected = true
        }
    }

    @Test
    fun shouldUpdateButtonOnUnVoteResponse() {
        val EXPECTED_VOTES = 115

        systemUnderTest.entryId = 12

        whenever(mockOfEntriesApi.unvoteEntry(any())).thenReturn(Single.just(VoteResponse(EXPECTED_VOTES, emptyList())))
        systemUnderTest.unvote()

        verify(mockOfView).apply {
            voteCount = EXPECTED_VOTES
            isButtonSelected = false
        }
    }

    @Test
    fun shouldShowErrorDialog() {
        systemUnderTest.entryId = 12
        whenever(mockOfEntriesApi.voteEntry(any())).thenReturn(Single.error(IOException()))
        whenever(mockOfEntriesApi.unvoteEntry(any())).thenReturn(Single.error(IOException()))

        systemUnderTest.vote()
        systemUnderTest.unvote()

        verify(mockOfView, times(2)).showErrorDialog(any())
    }
}