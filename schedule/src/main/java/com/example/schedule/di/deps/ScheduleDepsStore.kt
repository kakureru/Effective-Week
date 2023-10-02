package com.example.schedule.di.deps

import kotlin.properties.Delegates

object ScheduleDepsStore : ScheduleDepsProvider {
    override var deps: ScheduleDeps by Delegates.notNull()
}