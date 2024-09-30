package com.codingshuttle.project.uber.UberAppBackend.strategies;

import com.codingshuttle.project.uber.UberAppBackend.strategies.implementations.DriverMatchingHighestRatedDriverStrategy;
import com.codingshuttle.project.uber.UberAppBackend.strategies.implementations.DriverMatchingNearestDriverStrategy;
import com.codingshuttle.project.uber.UberAppBackend.strategies.implementations.RideFareDefaultFareCalculationStrategy;
import com.codingshuttle.project.uber.UberAppBackend.strategies.implementations.RideFareSurgePricingFareCalculationStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Component
@RequiredArgsConstructor
public class RideStrategyManager {

    private final DriverMatchingHighestRatedDriverStrategy highestRatedDriverStrategy;
    private final DriverMatchingNearestDriverStrategy nearestDriverStrategy;
    private final RideFareSurgePricingFareCalculationStrategy surgePricingFareCalculationStrategy;
    private final RideFareDefaultFareCalculationStrategy defaultFareCalculationStrategy;


    public DriverMatchingStrategy driverMatchingStrategy(double riderRating){
        if (riderRating >= 4.3) {
            return highestRatedDriverStrategy;
        } else {
            return nearestDriverStrategy;
        }
//        if(riderRating >= 4.8){
//            return highestRatedDriverStrategy;
//        }
//        else{
//            return nearestDriverStrategy;
//        }

    }
    public RideFareCalculationStrategy rideFareCalculationStrategy(){

        // 6PM to 9PM is SURGE TIME

//        LocalTime surgeStartTime = LocalTime.of(18,0);
//        LocalTime surgeEndTime = LocalTime.of(21,0);
//        LocalTime currentTime = LocalTime.now();
//
//        boolean isSurgeTime = currentTime.isAfter(surgeStartTime) && currentTime.isBefore(surgeEndTime);
//
//        if(isSurgeTime){
//            return surgePricingFareCalculationStrategy;
//        }
//        else {
//            return defaultFareCalculationStrategy;
//        }
        LocalTime morningSurgeStartTime = LocalTime.of(9, 0);
        LocalTime morningSurgeEndTime = LocalTime.of(11, 0);
        LocalTime lateNightSurgeStartTime = LocalTime.of(1, 0);
        LocalTime lateNightSurgeEndTime = LocalTime.of(4, 0);
        LocalTime eveningSurgeStartTime = LocalTime.of(18, 0);
        LocalTime eveningSurgeEndTime = LocalTime.of(20, 0);
        LocalTime currentTime = LocalTime.now();

        // Check if current time falls within any surge time range
        boolean isSurgeTime = (
                (currentTime.isAfter(morningSurgeStartTime) && currentTime.isBefore(morningSurgeEndTime)) ||
                        (currentTime.isAfter(lateNightSurgeStartTime) && currentTime.isBefore(lateNightSurgeEndTime)) ||
                        (currentTime.isAfter(eveningSurgeStartTime) && currentTime.isBefore(eveningSurgeEndTime))
        );

        if (isSurgeTime) {
            return surgePricingFareCalculationStrategy;
        } else {
            return defaultFareCalculationStrategy;
        }
    }

        }