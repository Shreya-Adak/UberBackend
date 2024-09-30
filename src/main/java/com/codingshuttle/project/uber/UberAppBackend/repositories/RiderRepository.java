package com.codingshuttle.project.uber.UberAppBackend.repositories;

import com.codingshuttle.project.uber.UberAppBackend.entities.Rider;
import com.codingshuttle.project.uber.UberAppBackend.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RiderRepository extends JpaRepository<Rider,Long> {
    Optional<Rider> findByUser(User user);
}
