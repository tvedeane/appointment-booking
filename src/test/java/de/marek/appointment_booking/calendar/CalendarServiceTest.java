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
    void noAvailableSlotsIfStartInBookedTime() {
        var day = LocalDate.of(2025, 1, 5);
        var salesManager = new SalesManager(1L);
        var products = new String[]{"Heatpumps"};
        when(mockCalendarRepository.findByDate(day, products, "", "")).thenReturn(
            List.of(
                new Slot(1L, LocalDateTime.of(2025, 1, 5, 10, 0), LocalDateTime.of(2025, 1, 5, 11, 0), true, salesManager),
                new Slot(1L, LocalDateTime.of(2025, 1, 5, 10, 30), LocalDateTime.of(2025, 1, 5, 11, 30), false, salesManager)
            )
        );

        var result = underTest.countAvailableSlots(day, products, "", "");
        assertThat(result).isEmpty();
    }

    @Test
    void noAvailableSlotsIfEndInBookedTime() {
        var day = LocalDate.of(2025, 1, 5);
        var salesManager = new SalesManager(1L);
        var products = new String[]{"Heatpumps"};
        when(mockCalendarRepository.findByDate(day, products, "", "")).thenReturn(
            List.of(
                new Slot(1L, LocalDateTime.of(2025, 1, 5, 11, 0), LocalDateTime.of(2025, 1, 5, 12, 0), true, salesManager),
                new Slot(1L, LocalDateTime.of(2025, 1, 5, 10, 30), LocalDateTime.of(2025, 1, 5, 11, 30), false, salesManager)
            )
        );

        var result = underTest.countAvailableSlots(day, products, "", "");
        assertThat(result).isEmpty();
    }

    @Test
    void bookedEndDateEqualsToFreeStartDate() {
        var day = LocalDate.of(2025, 1, 5);
        var salesManager = new SalesManager(1L);
        var products = new String[]{"Heatpumps"};
        when(mockCalendarRepository.findByDate(day, products, "", "")).thenReturn(
            List.of(
                new Slot(1L, LocalDateTime.of(2025, 1, 5, 10, 0), LocalDateTime.of(2025, 1, 5, 11, 0), true, salesManager),
                new Slot(1L, LocalDateTime.of(2025, 1, 5, 11, 0), LocalDateTime.of(2025, 1, 5, 12, 0), false, salesManager)
            )
        );

        var result = underTest.countAvailableSlots(day, products, "", "");
        assertThat(result).containsExactly(
            new AvailableSlotsResponse(1L, LocalDateTime.of(2025, 1, 5, 10, 0))
        );
    }

    @Test
    void multipleManagersWithSlotsAtTheSameTime() {
        var day = LocalDate.of(2025, 1, 5);
        var salesManager1 = new SalesManager(1L);
        var salesManager2 = new SalesManager(2L);
        var products = new String[]{"Heatpumps"};
        when(mockCalendarRepository.findByDate(day, products, "", "")).thenReturn(
            List.of(
                new Slot(1L, LocalDateTime.of(2025, 1, 5, 10, 0), LocalDateTime.of(2025, 1, 5, 11, 0), false, salesManager1),
                new Slot(1L, LocalDateTime.of(2025, 1, 5, 10, 0), LocalDateTime.of(2025, 1, 5, 11, 0), false, salesManager2)
            )
        );

        var result = underTest.countAvailableSlots(day, products, "", "");

        assertThat(result).containsExactly(
            new AvailableSlotsResponse(2L, LocalDateTime.of(2025, 1, 5, 9, 0))
        );
    }

    @Test
    void multipleSlotsAreSorted() {
        var day = LocalDate.of(2025, 1, 5);
        var salesManager = new SalesManager(1L);
        var products = new String[]{"Heatpumps"};
        when(mockCalendarRepository.findByDate(day, products, "", "")).thenReturn(
            List.of(
                new Slot(1L, LocalDateTime.of(2025, 1, 5, 10, 30), LocalDateTime.of(2025, 1, 5, 11, 30), false, salesManager),
                new Slot(1L, LocalDateTime.of(2025, 1, 5, 11, 0), LocalDateTime.of(2025, 1, 5, 12, 0), false, salesManager)
            )
        );

        var result = underTest.countAvailableSlots(day, products, "", "");
        assertThat(result).containsExactly(
            new AvailableSlotsResponse(1L, LocalDateTime.of(2025, 1, 5, 9, 30)),
            new AvailableSlotsResponse(1L, LocalDateTime.of(2025, 1, 5, 10, 0))
        );
    }
}