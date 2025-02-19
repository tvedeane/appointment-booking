package de.marek.appointment_booking.calendar;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CalendarRepository extends JpaRepository<Slot, Long> {
   @Query("SELECT s FROM Slot s WHERE DATE(s.startDate) = :date AND DATE(s.endDate) = :date")
   List<Slot> findByDate(@Param("date") LocalDate date);
}
