package de.marek.appointment_booking.calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/calendar")
public class CalendarController {
    @Autowired
    private CalendarService calendarService;

    @PostMapping("/query")
    public ResponseEntity<List<AvailableSlotsResponse>> queryCalendar(@RequestBody AvailableSlotsRequest request) {
        return ResponseEntity.ok(calendarService.countAvailableSlots(
            request.date(), request.products(), request.language(), request.rating()));
    }
}
