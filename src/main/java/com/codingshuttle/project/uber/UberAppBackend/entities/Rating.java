package com.codingshuttle.project.uber.UberAppBackend.entities;

import jakarta.persistence.*;
import lombok.*;

@Builder
@Getter
@Setter
@Entity
@Table(indexes = {
        @Index(name = "idx_rating_rider",columnList = "rider_id"),
        @Index(name = "idx_rating_driver",columnList = "driver_id")

})
@NoArgsConstructor
@AllArgsConstructor
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Ride ride;

    @ManyToOne
    private Rider rider;

   @ManyToOne
   private Driver driver;

   private Integer driverRating;
    private Integer riderRating;


}
