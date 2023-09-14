package ru.coworking.test.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.coworking.test.project.model.Room;

@Repository
public interface RoomRepo extends JpaRepository<Room, Long> {
}
