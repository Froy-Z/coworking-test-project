package ru.coworking.test.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.coworking.test.project.model.Coworking;

import java.util.Optional;

@Repository
public interface CoworkingRepo extends JpaRepository<Coworking, Long> {
    Optional<Coworking> findByName(String name);
}
