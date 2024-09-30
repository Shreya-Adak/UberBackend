package com.codingshuttle.project.uber.UberAppBackend.services.implementations;


import com.codingshuttle.project.uber.UberAppBackend.services.DistanceService;

import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;


@Service
@Slf4j
public class DistanceServiceHereRoutingImpl implements DistanceService {




    private static final String Here_Routing_API_BASE_URL = "https://api.geoapify.com/v1/routing?waypoints=";


    @Override
    public Double calculateDistance(Point src, Point dest) {
        try {
            String uri = Here_Routing_API_BASE_URL + src.getY()+","+src.getX()+"|"+dest.getY()+","+dest.getX() + "&mode=drive&apiKey=d00dd1ba8c7f4d70b03dd04b15b45fb8";
            RestClient restClient = RestClient.create();
            HereRoutingResponseDto responseDto = restClient
                    .get()
                    .uri(uri)
                    .retrieve()
                    .body(HereRoutingResponseDto.class);


            System.out.println(responseDto.getFeatures().get(0));

            return responseDto.getFeatures().get(0).getProperties().getDistance()/1000.0;
        } catch (Exception e) {
            throw new RuntimeException("Error getting data from HereRouting "+e.getMessage());
        }
    }
}

@Data
@ToString
class HereRoutingResponseDto {

    private List<HereRoutingRoute> features;
}

@Data
class HereRoutingRoute {

    private HereRoutingProperties properties;


}

@Data
class HereRoutingProperties {
    private Long distance;
}
//curl https://api.geoapify.com/v1/routing?waypoints=50.96209827745463%2C4.414458883409225%7C50.429137079078345%2C5.00088081232559&mode=drive&apiKey=d00dd1ba8c7f4d70b03dd04b15b45fb8


//"https://router.project-osrm.org/route/v1/driving/";