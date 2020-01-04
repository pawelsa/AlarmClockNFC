package com.helpfulapps.domain.use_cases.stats

import com.helpfulapps.domain.helpers.singleOf
import com.helpfulapps.domain.repository.StatsRepository
import com.helpfulapps.domain.use_cases.BaseUseCaseTest
import com.helpfulapps.domain.use_cases.mockData.MockStats
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test

class GetAllStatsUseCaseTest : BaseUseCaseTest<GetAllStatsUseCase>() {

    private val _statsRepository: StatsRepository = mockk {}
    override val useCase: GetAllStatsUseCase = GetAllStatsUseCaseImpl(_statsRepository)

    @Test
    fun `should get all stats`() {
        every { _statsRepository.getAllStats() } returns singleOf { MockStats.defaultAnalysedStats }

        useCase()
            .test()
            .assertResult(MockStats.defaultAnalysedStats)
            .dispose()
    }

    @Test
    fun `should get empty list`() {
        every { _statsRepository.getAllStats() } returns singleOf { MockStats.emptyStatsList }

        useCase()
            .test()
            .assertResult(MockStats.emptyStatsList)
            .dispose()
    }

}