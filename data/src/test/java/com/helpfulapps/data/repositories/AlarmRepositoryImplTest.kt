package com.helpfulapps.data.repositories

import com.helpfulapps.data.db.alarm.dao.AlarmDao
import com.helpfulapps.data.mockData.MockData
import com.helpfulapps.domain.exceptions.AlarmException
import com.helpfulapps.domain.helpers.singleOf
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Single
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test


class AlarmRepositoryImplTest {

    private val alarmDao: AlarmDao = mockk {}
    private val alarmRepositoryImpl = AlarmRepositoryImpl(alarmDao)

    @Nested
    inner class GetAlarms {

        @Test
        fun `should return empty list`() {

            every { alarmDao.getAlarms() } returns Single.just(listOf())
            alarmRepositoryImpl
                .getAlarmsQuery()
                .test()
                .assertResult(emptyList())
                .dispose()
        }

        @Test
        fun `should return alarms`() {

            every { alarmDao.getAlarms() } returns Single.just(MockData.alarmDataList)

            alarmRepositoryImpl.getAlarms()
                .test()
                .assertResult(MockData.alarmList)
                .dispose()
        }
    }

    @Nested
    inner class GetAlarm {

        @Test
        fun `should obtain alarm`() {

            every { alarmDao.getSingleAlarm(any()) } returns singleOf { MockData.defaultDataAlarm }

            alarmRepositoryImpl.getAlarm(1)
                .test()
                .assertResult(MockData.defaultAlarm)
                .dispose()
        }

        @Test
        fun `should throw error, when no alarm found`() {

            every { alarmDao.getSingleAlarm(any()) } returns Single.error(NoSuchElementException())

            alarmRepositoryImpl.getAlarm(1)
                .test()
                .assertError(NoSuchElementException::class.java)
                .dispose()
        }
    }

    @Nested
    inner class AddAlarm {

        @Test
        fun `should add alarm`() {

            every { alarmDao.insert(any()) } returns singleOf { 1L }
            every { alarmDao.getSingleAlarm(any()) } returns singleOf { MockData.defaultDataAlarm }

            val alarm = alarmRepositoryImpl.addAlarm(
                MockData.defaultAlarm
            )

            alarm
                .test()
                .assertResult(MockData.defaultAlarm)
                .dispose()

        }

        @Test
        fun `should throw invalid row, when error occured when adding to db`() {
            every { alarmDao.insert(any()) } returns singleOf { -1L }

            alarmRepositoryImpl.addAlarm(MockData.defaultAlarm)
                .test()
                .assertError(AlarmException::class.java)
                .dispose()
        }

        @Test
        fun `should throw exception`() {
            every { alarmDao.insert(any()) } returns Single.error(Exception())

            alarmRepositoryImpl.addAlarm(MockData.defaultAlarm)
                .test()
                .assertError(Exception::class.java)
                .dispose()
        }
    }

    @Nested
    inner class RemoveAlarm {

        @Test
        fun `should not remove alarm when db table is empty`() {

            every { alarmDao.getSingleAlarm(any()) } returns Single.error(NoSuchElementException())

            alarmRepositoryImpl.removeAlarm(5)
                .test()
                .assertError(NoSuchElementException::class.java)
                .dispose()

            verify(exactly = 0) { alarmDao.delete(any()) }
        }

        @Test
        fun `should remove alarm test`() {

            every { alarmDao.delete(any()) } returns singleOf { true }
            every { alarmDao.getSingleAlarm(any()) } returns singleOf { MockData.defaultDataAlarm }

            alarmRepositoryImpl.removeAlarm(5)
                .test()
                .assertComplete()
                .dispose()

            verify(exactly = 1) { alarmDao.getSingleAlarm(any()) }
        }

        @Test
        fun `should not remove alarm that is not in db`() {

            every { alarmDao.getSingleAlarm(any()) } returns Single.error(NoSuchElementException())

            alarmRepositoryImpl.removeAlarm(2)
                .test()
                .assertError(NoSuchElementException::class.java)
                .dispose()

            verify(exactly = 0) { alarmDao.delete(any()) }
        }
    }

    @Nested
    inner class SwitchAlarm {

        @Test
        fun `should switch when there is alarm in db`() {

            every { alarmDao.update(any()) } returns singleOf { true }
            every { alarmDao.getSingleAlarm(any()) } returns singleOf { MockData.defaultDataAlarm }

            alarmRepositoryImpl.switchAlarm(5)
                .test()
                .assertResult(MockData.defaultAlarm)
                .dispose()
        }

        @Test
        fun `should not switch when there is no alarm in db`() {

            every { alarmDao.getSingleAlarm(any()) } returns Single.error(NoSuchElementException())

            alarmRepositoryImpl.switchAlarm(2)
                .test()
                .assertError(NoSuchElementException::class.java)
                .dispose()

            verify(exactly = 0) { alarmDao.update(any()) }
        }

        @Test
        fun `should not switch when db is empty`() {

            every { alarmDao.getSingleAlarm(any()) } returns Single.error(NoSuchElementException())

            alarmRepositoryImpl.switchAlarm(2)
                .test()
                .assertError(NoSuchElementException::class.java)
                .dispose()

            verify(exactly = 0) { alarmDao.update(any()) }
        }
    }

    @Nested
    inner class UpdateAlarm {

        @Test
        fun `should update when there is alarm in db`() {

            every { alarmDao.getSingleAlarm(any()) } returns singleOf { MockData.defaultDataAlarm }
            every { alarmDao.update(any()) } returns singleOf { true }

            alarmRepositoryImpl.updateAlarm(MockData.defaultAlarm)
                .test()
                .assertResult(MockData.defaultAlarm)
                .dispose()
        }

        @Test
        fun `should not update when there is no alarm in the db`() {

            every { alarmDao.update(any()) } returns Single.error(NoSuchElementException())

            alarmRepositoryImpl.updateAlarm(MockData.defaultAlarm)
                .test()
                .assertError(NoSuchElementException::class.java)
                .dispose()
            verify(exactly = 0) { alarmDao.getSingleAlarm(any()) }
        }
    }

}
