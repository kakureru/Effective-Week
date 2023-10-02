package com.example.schedule.di

import androidx.lifecycle.ViewModel
import com.example.schedule.di.deps.ScheduleDepsProvider

internal class ScheduleComponentViewModel : ViewModel() {
    val newScheduleComponent = DaggerScheduleComponent.factory().create(ScheduleDepsProvider.deps)
}