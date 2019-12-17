package com.helpfulapps.domain.use_cases.stats

import com.helpfulapps.domain.models.stats.AnalysedAlarmStats
import com.helpfulapps.domain.repository.StatsRepository
import com.helpfulapps.domain.use_cases.type.SingleUseCase
import io.reactivex.Single

interface GetAllStatsUseCase : SingleUseCase<AnalysedAlarmStats>

class GetAllStatsUseCaseImpl(private val statsRepository: StatsRepository) : GetAllStatsUseCase {
    override fun invoke(): Single<AnalysedAlarmStats> = statsRepository.getAllStats()
}