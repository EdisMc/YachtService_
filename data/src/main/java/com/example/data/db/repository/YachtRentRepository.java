package com.example.data.db.repository;

import com.example.data.db.entity.YachtRent;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface YachtRentRepository extends JpaRepository<YachtRent, Long> {
    List<YachtRent> getYachtRentsByYachtId(Long id);
    List<YachtRent> getYachtRentsByEmployeeId(Long id);
    List<YachtRent> findAll();
}
