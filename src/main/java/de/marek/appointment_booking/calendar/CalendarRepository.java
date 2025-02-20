package de.marek.appointment_booking.calendar;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CalendarRepository extends JpaRepository<Slot, Long> {
   @Query(value = """
       SELECT s.* FROM slots s
       JOIN sales_managers sm ON s.sales_manager_id = sm.id
       WHERE DATE(s.start_date) = :date AND DATE(s.end_date) = :date
       AND sm.products @> CAST(:products AS varchar[])
       AND :language=ANY(sm.languages)
       AND :rating=ANY(sm.customer_ratings)
       """, nativeQuery = true)
   List<Slot> findByDate(@Param("date") LocalDate date,
                         @Param("products") String[] products,
                         @Param("language") String language,
                         @Param("rating") String rating);
}
