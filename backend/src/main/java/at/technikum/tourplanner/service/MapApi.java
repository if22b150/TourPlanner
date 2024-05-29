package at.technikum.tourplanner.service;

import at.technikum.tourplanner.service.impl.RouteInfo;

import java.util.List;

public interface MapApi {

    String searchAddress(String text);
    RouteInfo searchDirection(String start, String end, String transportType);
//    void getMap();

}