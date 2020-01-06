package com.helpfulapps.domain.repository

interface RingtoneRepository {
    fun getDefaultRingtone(): Pair<String, String>
    fun getRingtones(): Array<Pair<String, String>>
}