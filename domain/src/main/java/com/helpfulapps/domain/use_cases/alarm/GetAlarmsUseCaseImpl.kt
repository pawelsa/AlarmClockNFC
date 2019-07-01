package com.helpfulapps.domain.use_cases.alarm

import com.helpfulapps.domain.models.alarm.Alarm
import com.helpfulapps.domain.repository.AlarmRepository
import com.helpfulapps.domain.repository.WeatherRepository
import com.helpfulapps.domain.use_cases.alarm.definition.GetAlarmsUseCase
import io.reactivex.Single

// todo IMPORTANT !!
/**
 * podzielić przychodzącą prognozę pogody na dni
 *
 * dla każdej godziny przeanalizować pogodę - temperatura, opady deszczy/śniegu
 *
 * zwracany obiekt powinien zawierać informację dla dnia i godzin
 *
 * alarm powinien wyświetlać infotmację dla całego dnia jako ważniejsze niż godzinowa - priority
 *
 *
 * */


class GetAlarmsUseCaseImpl(
    private val alarmRepository: AlarmRepository,
    private val weatherRepository: WeatherRepository
) : GetAlarmsUseCase {

    override fun invoke(): Single<List<Alarm>> =
        alarmRepository.getAlarms()//.zipWith(weatherRepository.getForecastForAlarms())
}