package com.brtrip.trip.domain

import com.brtrip.common.exceptions.NotFoundException
import io.kotlintest.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.jdbc.Sql
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.LocalDateTime

@SpringBootTest
@Transactional
@Sql("/truncate.sql")
internal class TripDeleterTest {

    @Autowired
    private lateinit var sut: TripDeleter

    @Autowired
    private lateinit var tripRepository: TripRepository

    @Autowired
    private lateinit var stopRepository: StopRepository

    @Test
    fun `여행 일정 삭제`() {
        // given
        val trip = tripRepository.save(
            Trip(
                userId = 1L,
                title = "first trip",
                startDate = LocalDate.of(2021,6,1),
                endDate = LocalDate.of(2021,6,5)
            )
        )

        val stops = stopRepository.saveAll(listOf(
            Stop(
                trip = trip,
                name = "central park",
                lat = 123,
                lng = 456,
                stoppedAt = LocalDateTime.of(2021,6,3,0,0,0),
                sequence = 1
            ),
            Stop(
                trip = trip,
                name = "grand canyon",
                lat = 789,
                lng = 101,
                stoppedAt = LocalDateTime.of(2021,6,4,0,0,0),
                sequence = 2
            )
        ))
        trip.stops = stops

        // when
        sut.delete(trip.id!!)
        val deleted = tripRepository.findById(trip.id!!)
            .orElseThrow { NotFoundException("여행 일정이 없습니다.") }

        // then
        deleted.deleted shouldBe true
        deleted.stops.forEach {
            it.deleted shouldBe true
        }
    }
}
