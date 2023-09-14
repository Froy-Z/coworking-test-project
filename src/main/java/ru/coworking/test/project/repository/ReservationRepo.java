package ru.coworking.test.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.coworking.test.project.model.Reservation;

@Repository
public interface ReservationRepo extends JpaRepository<Reservation, Long> {
}
