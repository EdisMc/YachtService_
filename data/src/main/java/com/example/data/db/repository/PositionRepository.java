package com.example.data.db.repository;

import com.example.data.db.entity.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PositionRepository extends JpaRepository<Position, Long> {
    Optional<Position> getPositionById(Long id);
}
