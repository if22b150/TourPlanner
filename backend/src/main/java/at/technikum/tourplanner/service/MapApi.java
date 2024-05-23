package at.technikum.tourplanner.service;

import java.util.List;

public interface MapApi {

    String searchAddress(String text);
    List<double[]> searchDirection(String start, String end);
    void getMap();

}