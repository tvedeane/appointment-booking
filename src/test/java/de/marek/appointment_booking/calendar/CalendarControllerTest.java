package de.marek.appointment_booking.calendar;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CalendarController.class)
public class CalendarControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private CalendarService calendarService;

    @Test
    void queryCalendar() throws Exception {
        when(calendarService.findAvailableSlots(any(), any()))
            .thenReturn(List.of(new AvailableSlotsResponse(1L, LocalDateTime.now())));
        var content = "{\"date\": \"2024-05-04\", \"products\": [], \"language\": \"\", \"rating\": \"\"}";
        mockMvc.perform(MockMvcRequestBuilders.post("/calendar/query")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].available_count").exists());
    }
}
