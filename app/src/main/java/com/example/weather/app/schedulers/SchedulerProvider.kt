package com.example.weather.app.schedulers

import io.reactivex.Scheduler

interface SchedulerProvider {
    fun io(): Scheduler
    fun ui(): Scheduler
    fun computation(): Scheduler
    fun single(): Scheduler
}