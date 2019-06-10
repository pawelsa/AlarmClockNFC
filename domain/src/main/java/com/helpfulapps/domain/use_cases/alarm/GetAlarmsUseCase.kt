package com.helpfulapps.domain.use_cases.alarm

import com.helpfulapps.domain.models.alarm.Alarm
import com.helpfulapps.domain.repository.AlarmRepository
import com.helpfulapps.domain.use_cases.type.SingleUseCase
import io.reactivex.Single

class GetAlarmsUseCase(private val repository: AlarmRepository) : SingleUseCase<List<Alarm>> {

    override fun execute(): Single<List<Alarm>> = repository.getAlarms()
}