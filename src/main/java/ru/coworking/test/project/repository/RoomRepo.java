package ru.coworking.test.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.coworking.test.project.model.Room;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepo extends JpaRepository<Room, Long> {
    Optional<Room> findByIdAndCoworkingId(Long roomId, Long coworkingId);

    List<Room> findByCoworkingId(Long coworkingId);
}
