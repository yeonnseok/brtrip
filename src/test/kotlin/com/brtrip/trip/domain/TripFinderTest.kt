package com.brtrip.trip.domain

import com.brtrip.TestDataLoader
import com.brtrip.place.Place
import io.kotlintest.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.jdbc.Sql
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.time.LocalDate

@SpringBootTest
@Transactional
@Sql("/truncate.sql")
internal class TripFinderTest {

    @Autowired
    private lateinit var sut: TripFinder

    @Autowired
    lateinit var testDataLoader: TestDataLoader

    @Test
    fun `내 모든 여행 일정 조회`() {
        // given
        val trip = testDataLoader.sample_trip_first(1L)
        val stops = testDataLoader.sample_stops_first(trip)
        trip.stops = stops

        // when
        val trips = sut.findByUserId(1L)

        // then
        trips.size shouldBe 1
        trips[0].title shouldBe "first trip"
        trips[0].stops.size shouldBe 2
        trips[0].stops[0].place.name shouldBe "central park"
        trips[0].stops[1].place.name shouldBe "grand canyon"
    }

    @Test
    fun `내 최근 여행 일정 조회`() {
        // given
        val trip = testDataLoader.sample_trip_first(1L)
        val stops = testDataLoader.sample_stops_first(trip)
        trip.stops = stops

        // when
        val result = sut.findRecent(1L)

        // then
        result.title shouldBe "first trip"
        result.stops.size shouldBe 2
        result.stops[0].place.name shouldBe "central park"
        result.stops[1].place.name shouldBe "grand canyon"
    }
}
