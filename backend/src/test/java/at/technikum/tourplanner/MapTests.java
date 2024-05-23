package at.technikum.tourplanner;

import at.technikum.tourplanner.service.MapApi;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@SpringBootTest
@Transactional
public class MapTests {
    @Autowired
    private MapApi mapApi;

    @Test
    void test_searchAddress() {
        String coordinates1 = mapApi.searchAddress("Austria, 1200 Wien, Höchstädtplatz");
        String coordinates2 = mapApi.searchAddress("Austria, 1020 Wien, Praterstern");
        System.out.println(coordinates1);
        // start: 16.381029,48.235378
        // end: 16.392599,48.22038
        List<double[]> routes = mapApi.searchDirection(coordinates1, coordinates2);

        AtomicInteger i = new AtomicInteger();
        StringBuffer sb = new StringBuffer();
        routes.forEach(r -> {
            if (i.get() > 0) {
                sb.append(";");
            }
            if (i.getAndIncrement() % 5 == 0) {
                sb.append("\n");
            }
            sb.append(String.format("[%f; %f]", r[0], r[1]));
        });
        System.out.println();
        String routesAsString = sb.toString();
        routesAsString = routesAsString.replace(",",".").replace(";",",");
        System.out.println(routesAsString);
        System.out.println();
        System.out.printf("start: %s\n", coordinates1);
        System.out.printf("end: %s\n", coordinates2);
        double[] center = calculateMapCenter(routes);
        System.out.printf("center: %f, %f", center[0], center[1]);


    }

    public static double[] calculateMapCenter(List<double[]> routeCoordinates) {
        double minLat = Double.MAX_VALUE;
        double maxLat = Double.MIN_VALUE;
        double minLng = Double.MAX_VALUE;
        double maxLng = Double.MIN_VALUE;

        for (double[] coords : routeCoordinates) {
            double lat = coords[0];
            double lng = coords[1];

            if (lat < minLat) minLat = lat;
            if (lat > maxLat) maxLat = lat;
            if (lng < minLng) minLng = lng;
            if (lng > maxLng) maxLng = lng;
        }

        double centerLat = (minLat + maxLat) / 2;
        double centerLng = (minLng + maxLng) / 2;

        return new double[]{centerLat, centerLng};
    }
}
