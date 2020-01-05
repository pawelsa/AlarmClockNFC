package com.helpfulapps.data.repositories

import com.helpfulapps.data.db.stats.dao.StatsDao
import com.helpfulapps.data.db.stats.model.AlarmStatsData
import com.helpfulapps.data.mockData.MockStats
import com.helpfulapps.domain.helpers.singleOf
import com.helpfulapps.domain.models.stats.AlarmStats
import io.mockk.every
import io.mockk.mockk
import io.reactivex.Single
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class StatsRepositoryImplTest {

    private val statsDao: StatsDao = mockk {}
    private val statsRepository = StatsRepositoryImpl(statsDao)

    @Nested
    inner class GetAllStats {

        @Test
        fun `should get all stats`() {
            every { statsDao.getAll() } returns singleOf { MockStats.alarmStatsList }

            statsRepository.getAllStats()
                .test()
                .assertResult(MockStats.defaultAnalysedStats)
                .dispose()
        }

        @Test
        fun `should get empty list`() {
            every { statsDao.getAll() } returns singleOf { listOf<AlarmStatsData>() }

            statsRepository.getAllStats()
                .test()
                .assertResult(MockStats.emptyStatsList)
                .dispose()
        }

    }

    @Nested
    inner class SaveInfo {

        @Test
        fun `should save successfully`() {
            every { statsDao.save(any()) } returns singleOf { true }

            statsRepository.saveInfo(AlarmStats(1, 1, 1, 1, 1))
                .test()
                .assertComplete()
                .dispose()
        }

        @Test
        fun `should save fail, but return completed`() {
            every { statsDao.save(any()) } returns singleOf { false }

            statsRepository.saveInfo(AlarmStats(1, 1, 1, 1, 1))
                .test()
                .assertComplete()
                .dispose()
        }

        @Test
        fun `should save throw error, but return completed`() {
            every { statsDao.save(any()) } returns Single.error(NoSuchElementException())

            statsRepository.saveInfo(AlarmStats(1, 1, 1, 1, 1))
                .test()
                .assertComplete()
                .dispose()
        }

    }

}