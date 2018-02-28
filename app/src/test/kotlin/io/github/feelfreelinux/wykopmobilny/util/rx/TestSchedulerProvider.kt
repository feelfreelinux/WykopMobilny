package io.github.feelfreelinux.wykopmobilny.util.rx

import io.github.feelfreelinux.wykopmobilny.base.Schedulers
import io.reactivex.Scheduler
import io.reactivex.schedulers.TestScheduler


/**
 * Created by hrskrs on 5/8/2017.
 */

class TestSchedulerProvider() : Schedulers {

	val mTestScheduler: TestScheduler = TestScheduler()
	override fun mainThread(): Scheduler {
		return mTestScheduler
	}

	override fun backgroundThread(): Scheduler {
		return mTestScheduler
	}

}