package com.example.trackit.repository;

import com.example.trackit.model.entity.Saving;
import com.example.trackit.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SavingRepository extends JpaRepository<Saving, Long> {
    Optional<Saving> findByGoal(String goal);

    List<Saving> findByUser(User user);
}
