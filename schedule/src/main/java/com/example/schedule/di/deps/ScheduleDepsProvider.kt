package com.example.schedule.di.deps

interface ScheduleDepsProvider {
    val deps: ScheduleDeps
    companion object : ScheduleDepsProvider by ScheduleDepsStore
}