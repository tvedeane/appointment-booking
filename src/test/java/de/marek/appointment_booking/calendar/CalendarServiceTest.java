package de.marek.appointment_booking.calendar;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CalendarServiceTest {
    private final CalendarRepository mockCalendarRepository = mock();

    CalendarService underTest = new CalendarService(mockCalendarRepository);

    @Test
    void noAvailableSlotsIfOverlap() {
        var day = LocalDate.of(2025, 1, 5);
        when(mockCalendarRepository.findByDate(day)).thenReturn(
            List.of(
                new Slot(1L, LocalDateTime.of(2025, 1, 5, 10, 0), LocalDateTime.of(2025, 1, 5, 11, 0), true, 1L),
                new Slot(1L, LocalDateTime.of(2025, 1, 5, 10, 30), LocalDateTime.of(2025, 1, 5, 11, 30), false, 1L)
            )
        );

        var result = underTest.findAvailableSlots(day);
        assertThat(result).isEmpty();
    }

    @Test
    void bookedEndDateEqualsToFreeStartDate() {
        var day = LocalDate.of(2025, 1, 5);
        when(mockCalendarRepository.findByDate(day)).thenReturn(
            List.of(
                new Slot(1L, LocalDateTime.of(2025, 1, 5, 10, 0), LocalDateTime.of(2025, 1, 5, 11, 0), true, 1L),
                new Slot(1L, LocalDateTime.of(2025, 1, 5, 11, 0), LocalDateTime.of(2025, 1, 5, 12, 0), false, 1L)
            )
        );

        var result = underTest.findAvailableSlots(day);
        assertThat(result).containsExactly(
            new AvailableSlotsResponse(1L, LocalDateTime.of(2025, 1, 5, 11, 0))
        );
    }

    @Test
    void multipleManagersWithSlotsAtTheSameTime() {
        var day = LocalDate.of(2025, 1, 5);
        when(mockCalendarRepository.findByDate(day)).thenReturn(
            List.of(
                new Slot(1L, LocalDateTime.of(2025, 1, 5, 10, 0), LocalDateTime.of(2025, 1, 5, 11, 0), false, 1L),
                new Slot(1L, LocalDateTime.of(2025, 1, 5, 10, 0), LocalDateTime.of(2025, 1, 5, 11, 0), false, 2L)
            )
        );

        var result = underTest.findAvailableSlots(day);

        assertThat(result).containsExactly(
            new AvailableSlotsResponse(2L, LocalDateTime.of(2025, 1, 5, 10, 0))
        );
    }
}