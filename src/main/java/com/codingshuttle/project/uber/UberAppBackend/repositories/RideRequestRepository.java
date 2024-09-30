package com.codingshuttle.project.uber.UberAppBackend.repositories;

import com.codingshuttle.project.uber.UberAppBackend.entities.RideRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RideRequestRepository extends JpaRepository<RideRequest,Long> {
}
