package com.helpfulapps.domain.use_cases.alarm

import com.helpfulapps.domain.helpers.TimeSetter
import com.helpfulapps.domain.models.alarm.WeatherAlarm
import com.helpfulapps.domain.repository.AlarmRepository
import com.helpfulapps.domain.repository.WeatherRepository
import com.helpfulapps.domain.use_cases.type.SingleUseCaseWithParameter
import io.reactivex.Single

interface GetAlarmUseCase : SingleUseCaseWithParameter<GetAlarmUseCase.Params, WeatherAlarm> {
    data class Params(val alarmId: Long)
}

// TODO WRITE TESTS
class GetAlarmUseCaseImpl(
    private val alarmRepository: AlarmRepository,
    private val _weatherRepository: WeatherRepository
) : GetAlarmUseCase {
    override fun invoke(parameter: GetAlarmUseCase.Params): Single<WeatherAlarm> {
        return alarmRepository.getAlarm(parameter.alarmId)
            .flatMap {
                val timeSetter = TimeSetter()
                val startingTime = timeSetter.getAlarmStartingPoint(it)
                return@flatMap _weatherRepository.getForecastForAlarm(startingTime)
                    .map { weather ->
                        WeatherAlarm(it, weather)
                    }
            }
    }
}