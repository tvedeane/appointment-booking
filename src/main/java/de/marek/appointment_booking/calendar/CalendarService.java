package de.marek.appointment_booking.calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CalendarService {
    @Autowired
    private final CalendarRepository calendarRepository;

    public CalendarService(CalendarRepository calendarRepository) {
        this.calendarRepository = calendarRepository;
    }

    public List<AvailableSlotsResponse> findAvailableSlots(LocalDate day,
                                                           String[] products,
                                                           String language,
                                                           String rating) {
        var allSlots = calendarRepository.findByDate(day, products, language, rating);
        var slotsPerManager = allSlots.stream().collect(Collectors.groupingBy(s -> s.getSalesManager().getId()));

        var availableSlotsPerTime = slotsPerManager.values().stream()
            .map(this::countAvailableSlots)
            .flatMap(List::stream)
            .collect(Collectors.groupingBy(Slot::getStartDate, Collectors.counting()));

        return availableSlotsPerTime.entrySet().stream()
            .map(s -> new AvailableSlotsResponse(s.getValue(), toUTC(s.getKey()))).toList();
    }

    private List<Slot> countAvailableSlots(List<Slot> slots) {
        var grouped = slots.stream().collect(Collectors.groupingBy(Slot::getBooked));
        var bookedSlots = grouped.getOrDefault(true, Collections.emptyList());
        var availableSlots = grouped.getOrDefault(false, Collections.emptyList());

        var bookedStartTimes = bookedSlots.stream()
            .map(Slot::getStartDate)
            .collect(Collectors.toSet());

        return availableSlots.stream()
            .filter(slot -> isAvailable(slot.getStartDate(), bookedStartTimes))
            .toList();
    }

    private boolean isAvailable(LocalDateTime startTime, Set<LocalDateTime> bookedStartTimes) {
        var endTime = startTime.plusHours(1);
        return bookedStartTimes.stream()
            .noneMatch(bookedStart -> {
                LocalDateTime bookedEnd = bookedStart.plusHours(1);

                // Check if  start time falls within booked slot's hour, OR
                // end time falls within booked slot's hour (exclusive)
                return (startTime.isAfter(bookedStart) && startTime.isBefore(bookedEnd)) ||
                       (endTime.isAfter(bookedStart) && endTime.isBefore(bookedEnd)) ||
                       startTime.equals(bookedStart);
            });
    }

    private LocalDateTime toUTC(LocalDateTime time) {
        return time.atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime();
    }
}
