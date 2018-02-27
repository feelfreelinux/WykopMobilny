package io.github.feelfreelinux.wykopmobilny.utils.rx

import io.reactivex.Scheduler

/**
 * Created by altafshaikh on 27/02/18.
 */

interface SchedulerProvider {
	fun ui(): Scheduler
	fun computation(): Scheduler
	fun io(): Scheduler
}