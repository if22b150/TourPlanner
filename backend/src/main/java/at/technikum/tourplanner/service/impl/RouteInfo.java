package at.technikum.tourplanner.service.impl;

import at.technikum.tourplanner.service.map.MapCreator;

import java.util.List;

public class RouteInfo {
    private List<double[]> coordinates;
    private double duration;
    private double distance;

    public RouteInfo(List<double[]> coordinates, double duration, double distance) {
        this.coordinates = coordinates;
        this.duration = duration;
        this.distance = distance;
    }

    public List<double[]> getCoordinates() {
        return coordinates;
    }

    public double getDuration() {
        return duration;
    }

    public double getDistance() {
        return distance;
    }
}