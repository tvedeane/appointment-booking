package de.marek.appointment_booking;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/calendar")
public class CalendarController {
    @PostMapping("/query")
    public String queryCalendar() {
        return """
            [
            {
            "available_count": 1,
            "start_date": "2024-05-03T10:30:00.00Z"
            },
            {
            "available_count": 2,
            "start_date": "2024-05-03T12:00:00.00Z"
            }
            ]
            """;
    }
}
