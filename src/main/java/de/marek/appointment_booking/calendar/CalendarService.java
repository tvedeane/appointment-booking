package de.marek.appointment_booking.calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CalendarService {
    @Autowired
    private final CalendarRepository calendarRepository;

    public CalendarService(CalendarRepository calendarRepository) {
        this.calendarRepository = calendarRepository;
    }

    public List<AvailableSlotsResponse> findAvailableSlots(LocalDate day, String[] products) {
        var allSlots = calendarRepository.findByDate(day, products);
        var slotsPerManager = allSlots.stream().collect(Collectors.groupingBy(s -> s.getSalesManager().getId()));

        var availableSlotsPerTime = slotsPerManager.values().stream()
            .map(this::countAvailableSlots)
            .flatMap(List::stream)
            .collect(Collectors.groupingBy(Slot::getStartDate, Collectors.counting()));

        return availableSlotsPerTime.entrySet().stream()
            .map(s -> new AvailableSlotsResponse(s.getValue(), s.getKey()))
            .toList();
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
        return bookedStartTimes.stream()
            .noneMatch(bookedStart ->
                !startTime.isBefore(bookedStart) &&
                startTime.isBefore(bookedStart.plusHours(1)));
    }
}
