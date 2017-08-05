package io.github.feelfreelinux.wykopmobilny.utils

import io.github.feelfreelinux.wykopmobilny.objects.VoteResponse

typealias TagClickedListener = (String) -> Unit
typealias VoteResponseListener = (VoteResponse) -> Unit
typealias CommentClickedListener = (Int) -> Unit
typealias ApiResponseCallback = (Any) -> Unit
typealias EmptyListener = () -> Unit
typealias DataLoadedListener = (Boolean) -> Unit // Refreshed / Not Refreshed