package at.technikum.tourplanner.service.map;

import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.imageio.ImageIO;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
public class MapCreator {

    public record GeoCoordinate(double lon, double lat) {}

    public enum Marker {
        PIN_RED_16px("pin-red_16px"),
        PIN_RED_32px("pin-red_32px"),
        MARKER_RED_16px("marker-red_16px"),
        MARKER_RED_32px( "marker-red_32px");

        Marker(String filename) {
            this.filename = filename;
        }

        public final String filename;

        public URL getResource() {
            return getClass().getResource("/" + Marker.PIN_RED_32px.filename + ".png");
        }
    }

    private final double minLon;
    private final double minLat;
    private final double maxLon;
    private final double maxLat;

    @Setter
    private int zoom = 18;
    @Setter
    private boolean cropImage = true;
    @Setter
    private List<double[]> routeCoordinates = new ArrayList<>();

    private final List<GeoCoordinate> markers = new ArrayList<>();

    private BufferedImage finalImage;


    public MapCreator(double minLon, double minLat, double maxLon, double maxLat) {
        this.minLon = minLon;
        this.minLat = minLat;
        this.maxLon = maxLon;
        this.maxLat = maxLat;
    }


    public void generateImage() throws IOException {
        // Calculate the tile numbers for each corner of the bounding box
        var topLeftTile = TileCalculator.latlon2Tile(maxLat, minLon, zoom);
        var bottomRightTile = TileCalculator.latlon2Tile(minLat, maxLon, zoom);

        // Determine the number of tiles to fetch in each dimension
        int tilesX = bottomRightTile.x() - topLeftTile.x() + 1;
        int tilesY = bottomRightTile.y() - topLeftTile.y() + 1;

        // Create a new image to hold all the tiles
        finalImage = new BufferedImage(tilesX * 256, tilesY * 256, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = (Graphics2D) finalImage.getGraphics();

        // Fetch and draw each tile
        for (int x = topLeftTile.x(); x <= bottomRightTile.x(); x++) {
            for (int y = topLeftTile.y(); y <= bottomRightTile.y(); y++) {
                BufferedImage tileImage = fetchTile(zoom, x, y);
                int xPos = (x - topLeftTile.x()) * 256;
                int yPos = (y - topLeftTile.y()) * 256;
                g.drawImage(tileImage, xPos, yPos, null);
            }
        }

        PixelCalculator.Point topLeftTilePixel = new PixelCalculator.Point(topLeftTile.x() * 256, topLeftTile.y() * 256);

        // Draw Markers
        for (var marker : markers) {
            BufferedImage markerIcon = ImageIO.read(Marker.PIN_RED_32px.getResource());
            PixelCalculator.Point globalPos = PixelCalculator.latLonToPixel(marker.lat(), marker.lon(), zoom);
            int markerX = globalPos.x() - topLeftTilePixel.x();
            int markerY = globalPos.y() - topLeftTilePixel.y();
            g.drawImage(markerIcon, markerX, markerY, null);
        }

        // Draw Path
        if (!routeCoordinates.isEmpty()) {
            g.setColor(Color.RED); // Set the color for the path
            g.setStroke(new BasicStroke(2)); // Set the stroke for the path

            PixelCalculator.Point prevPixel = null;
            for (double[] coord : routeCoordinates) {
                PixelCalculator.Point currentPixel = PixelCalculator.latLonToPixel(coord[1], coord[0], zoom); // Note: coord[1] is latitude and coord[0] is longitude
                int currentX = currentPixel.x() - topLeftTilePixel.x();
                int currentY = currentPixel.y() - topLeftTilePixel.y();
                System.out.println("Current coordinate: (" + coord[1] + ", " + coord[0] + ")");
                System.out.println("Current pixel: (" + currentX + ", " + currentY + ")");
                if (prevPixel != null) {
                    int prevX = prevPixel.x() - topLeftTilePixel.x();
                    int prevY = prevPixel.y() - topLeftTilePixel.y();
                    g.drawLine(prevX, prevY, currentX, currentY);
                    System.out.println("Drawing line from (" + prevX + ", " + prevY + ") to (" + currentX + ", " + currentY + ")");
                }
                prevPixel = currentPixel;
            }
        }

        // Crop the image to the exact bounding box
        if (cropImage) {
            PixelCalculator.Point bboxLeftTopGlobalPos = PixelCalculator.latLonToPixel(maxLat, minLon, zoom);
            PixelCalculator.Point bboxRightBottomGlobalPos = PixelCalculator.latLonToPixel(minLat, maxLon, zoom);
            PixelCalculator.Point bboxLeftTopRelativePos = new PixelCalculator.Point(bboxLeftTopGlobalPos.x() - topLeftTilePixel.x(), bboxLeftTopGlobalPos.y() - topLeftTilePixel.y());
            int width = bboxRightBottomGlobalPos.x() - bboxLeftTopGlobalPos.x();
            int height = bboxRightBottomGlobalPos.y() - bboxLeftTopGlobalPos.y();
            finalImage = finalImage.getSubimage(bboxLeftTopRelativePos.x(), bboxLeftTopRelativePos.y(), width, height);
        }

        g.dispose();
    }

    public void saveImage(String filename) throws IOException {
        // Save or manipulate the final image as needed
        ImageIO.write(finalImage, "png", new File(filename));
    }


    private static BufferedImage fetchTile(int zoom, int x, int y) throws IOException {
        String tileUrl = "https://tile.openstreetmap.org/" + zoom + "/" + x + "/" + y + ".png";
        URL url = new URL(tileUrl);
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        httpConn.addRequestProperty("User-Agent", "CarSharingTest/1.0 Demo application for the SAM-course");

        try (InputStream inputStream = httpConn.getInputStream()) {
            return ImageIO.read(inputStream);
        } finally {
            httpConn.disconnect();
        }
    }
//
//
//    public static void main(String[] args) throws Exception {
//        // Define the bounding box and zoom level
//        MapCreator mapCreator = new MapCreator( 16.375941, 48.239177, 16.378488, 48.240183 );
//        mapCreator.setZoom(18);
//        mapCreator.getMarkers().add( new GeoCoordinate(16.377229, 48.239676) );
//
//        mapCreator.generateImage();
//        mapCreator.saveImage("FHTW-map.png");
//
//    }
}
