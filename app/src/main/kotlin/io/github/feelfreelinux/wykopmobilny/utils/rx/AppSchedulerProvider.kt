package io.github.feelfreelinux.wykopmobilny.utils.rx

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


/**
 * Created by altafshaikh on 27/02/18.
 */

class AppSchedulerProvider : SchedulerProvider {

	override fun ui(): Scheduler {
		return AndroidSchedulers.mainThread()
	}

	override fun computation(): Scheduler {
		return Schedulers.computation()
	}

	override fun io(): Scheduler {
		return Schedulers.io()
	}

}