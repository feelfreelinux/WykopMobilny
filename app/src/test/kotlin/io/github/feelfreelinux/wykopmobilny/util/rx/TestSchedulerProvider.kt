package io.github.feelfreelinux.wykopmobilny.util.rx

import io.github.feelfreelinux.wykopmobilny.utils.rx.SchedulerProvider
import io.reactivex.Scheduler
import io.reactivex.schedulers.TestScheduler


/**
 * Created by hrskrs on 5/8/2017.
 */

class TestSchedulerProvider() : SchedulerProvider {

	val mTestScheduler: TestScheduler = TestScheduler()
	override fun ui(): Scheduler {
		return mTestScheduler
	}

	override fun computation(): Scheduler {
		return mTestScheduler
	}

	override fun io(): Scheduler {
		return mTestScheduler
	}

}