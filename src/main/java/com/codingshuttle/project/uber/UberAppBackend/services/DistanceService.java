package com.codingshuttle.project.uber.UberAppBackend.services;

import org.locationtech.jts.geom.Point;

public interface DistanceService {
    Double calculateDistance(Point src, Point dest);
}
