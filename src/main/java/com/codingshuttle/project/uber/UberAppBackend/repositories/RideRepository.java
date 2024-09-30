package com.codingshuttle.project.uber.UberAppBackend.repositories;

import com.codingshuttle.project.uber.UberAppBackend.entities.Driver;
import com.codingshuttle.project.uber.UberAppBackend.entities.Ride;
import com.codingshuttle.project.uber.UberAppBackend.entities.Rider;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RideRepository extends JpaRepository<Ride,Long> {
    Page<Ride> findByDriver(Driver driver, Pageable pageRequest);

    Page<Ride> findByRider(Rider rider, Pageable pageRequest);
}
