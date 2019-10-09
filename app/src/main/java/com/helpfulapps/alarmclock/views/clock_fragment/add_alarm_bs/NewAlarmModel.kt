package com.helpfulapps.alarmclock.views.clock_fragment.add_alarm_bs

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR

class NewAlarmModel : BaseObservable() {
    @Bindable
    var hour: Int = 8
        set(value) {
            field = value
            notifyPropertyChanged(BR.hour)
        }
    @Bindable
    var minute: Int = 30
        set(value) {
            field = value
            notifyPropertyChanged(BR.minute)
        }
    @Bindable
    var repeating: Boolean = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.repeating)
        }
    var repeatingDays: Array<Boolean> = Array(7) { false }
    var song: Int = 0
    @Bindable
    var vibrations: Boolean = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.vibrations)
        }

}