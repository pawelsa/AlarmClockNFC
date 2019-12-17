package com.example.db

import com.raizlabs.android.dbflow.annotation.Database


@Database(name = AlarmAppDatabase.NAME, version = AlarmAppDatabase.VERSION)
object AlarmAppDatabase {

    const val NAME: String = "AlarmAppDB"
    const val VERSION: Int = 1
}