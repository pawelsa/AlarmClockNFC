package com.helpfulapps.domain.use_cases.stats

import com.helpfulapps.domain.models.stats.AlarmStats
import com.helpfulapps.domain.repository.StatsRepository
import com.helpfulapps.domain.use_cases.type.SingleUseCase
import io.reactivex.Single

interface GetAllStatsUseCase : SingleUseCase<List<AlarmStats>>

class GetAllStatsUseCaseImpl(private val statsRepository: StatsRepository) : GetAllStatsUseCase {
    override fun invoke(): Single<List<AlarmStats>> = statsRepository.getAllStats()
}