package com.example.data.db.repository;

import com.example.data.db.entity.Yacht;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface YachtRepository extends JpaRepository<Yacht, Long> {
    List<Yacht> findAllByStatus(Boolean status);
    Optional<Yacht> findYachtByStatusAndNumber(Boolean status, String number);
    Optional<Yacht> findYachtByNumber(String number);
    List<Yacht> findAll();
}
