package at.technikum.tourplanner.service.impl;

import at.technikum.tourplanner.service.MapApi;
import at.technikum.tourplanner.service.map.MapCreator;
import at.technikum.tourplanner.service.map.TileCalculator;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static at.technikum.tourplanner.service.map.TileCalculator.latlon2Tile;

@Component
public class MapApiImpl implements MapApi {

    private static final String API_KEY = "5b3ce3597851110001cf62481b4ca148561342b1912b5c2b4712073d";

    @Override
    public String searchAddress(String text) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(
                "https://api.openrouteservice.org/geocode/search?boundary.country=AT&api_key=" + API_KEY + "&text=" + text,
                String.class);

        JSONArray features = new JSONObject(response.getBody()).getJSONArray("features");
        if (features.length() < 1) {
            return null;
        }
        String coordinate = response.getBody().substring(response.getBody().indexOf("coordinates") + 14, response.getBody().indexOf("properties") - 4);
        System.out.println(coordinate);

        return coordinate;
    }

    @Override
    public RouteInfo searchDirection(String start, String end, String transportType) {
        String profile;
        switch (transportType) {
            case "Auto":
                profile = "driving-car";
                break;
            case "Fahrrad":
                profile = "cycling-regular";
                break;
            default:
                profile = "foot-walking";
        }
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(
                "https://api.openrouteservice.org/v2/directions/" + profile + "?api_key=" + API_KEY + "&start=" + start + "&end=" + end,
                String.class);

        List<double[]> routeCoordinates = new ArrayList<>();
        double duration = 0;
        double distance = 0;

        if (response.getStatusCode() == HttpStatus.OK) {
            JSONObject jsonResponse = new JSONObject(response.getBody());
            System.out.println(jsonResponse);

            JSONArray features = jsonResponse.getJSONArray("features");
            if (features.length() < 1) {
                return null;
            }

            JSONObject properties = features.getJSONObject(0).getJSONObject("properties");
            JSONObject summary = properties.getJSONObject("summary");
            duration = summary.getDouble("duration");
            distance = summary.getDouble("distance");

            JSONObject geometry = features.getJSONObject(0).getJSONObject("geometry");
            JSONArray coordinates = geometry.getJSONArray("coordinates");

            // Swapping longitude and latitude for each coordinate
            for (int i = 0; i < coordinates.length(); i++) {
                JSONArray coord = coordinates.getJSONArray(i);
                double longitude = coord.getDouble(0);
                double latitude = coord.getDouble(1);
                routeCoordinates.add(new double[]{latitude, longitude});
            }

            JSONArray bbox = features.getJSONObject(0).getJSONArray("bbox");
//            this.getMap(routeCoordinates, Double.parseDouble(start.split(",")[0]), Double.parseDouble(start.split(",")[1]),Double.parseDouble( end.split(",")[0]), Double.parseDouble(end.split(",")[1]), bbox.getDouble(0),bbox.getDouble(1),bbox.getDouble(2),bbox.getDouble(3),18);
        }

        return new RouteInfo(routeCoordinates, duration, distance);
    }

//    @Override
    public void getMap(List<double[]> routeCoordinates, Double startLat, Double startLon, Double endLat, Double endLon, Double bbox1, Double bbox2, Double bbox3, Double bbox4, Integer zoom) {
        // Define the bounding box and zoom level
//        MapCreator mapCreator = new MapCreator( 16.375941, 48.239177, 16.378488, 48.240183);
        MapCreator mapCreator = new MapCreator( bbox1, bbox2, bbox3, bbox4 );
        mapCreator.setZoom(zoom);
        mapCreator.setRouteCoordinates(routeCoordinates);
        mapCreator.getMarkers().add( new MapCreator.GeoCoordinate(startLon, startLat) );
        mapCreator.getMarkers().add( new MapCreator.GeoCoordinate(endLon, endLat) );

        try {
            mapCreator.generateImage();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            mapCreator.saveImage("FHTW-map.png");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}

