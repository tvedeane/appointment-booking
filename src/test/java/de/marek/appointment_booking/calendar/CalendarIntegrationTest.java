package de.marek.appointment_booking.calendar;

import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.MountableFile;

import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CalendarIntegrationTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Container
    @ServiceConnection
    static PostgreSQLContainer postgres = new PostgreSQLContainer<>("postgres:16")
        .withCopyFileToContainer(
            MountableFile.forHostPath(Paths.get("db/init.sql").toAbsolutePath()),
            "/docker-entrypoint-initdb.d/init.sql"
        );

    @BeforeAll
    static void setup() {
        postgres.start();
    }

    @AfterAll
    static void tearDown() {
        postgres.stop();
    }

    @Test
    void queryCalendar() {
        var content = new AvailableSlotsRequest(
            LocalDate.of(2024, 5, 3),
            new String[]{"SolarPanels"},
            "German",
            "Bronze");

        var response = restTemplate.postForEntity(
            "/calendar/query",
            content,
            List.class);

        var body = response.getBody();

        //noinspection unchecked
        assertThat(body).extracting("available_count", "start_date").contains(
            Tuple.tuple(1, "2024-05-03T10:30:00.000Z"),
            Tuple.tuple(1, "2024-05-03T11:00:00.000Z"),
            Tuple.tuple(1, "2024-05-03T11:30:00.000Z"));
    }
}
