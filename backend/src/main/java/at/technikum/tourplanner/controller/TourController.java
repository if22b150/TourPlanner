package at.technikum.tourplanner.controller;

import at.technikum.tourplanner.persistence.entity.TourEntity;
import at.technikum.tourplanner.persistence.entity.TourLogEntity;
import at.technikum.tourplanner.service.TourLogService;
import at.technikum.tourplanner.service.dto.TourDto;
import at.technikum.tourplanner.service.TourService;
import at.technikum.tourplanner.service.dto.TourLogDto;
import at.technikum.tourplanner.service.reports.PdfGenerator;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping(path = "tours")
public class TourController {
    @Autowired
    private TourService tourService;
    @Autowired
    private TourLogService tourLogService;

    @Autowired
    private PdfGenerator pdfGenerator;

    @GetMapping
    public List<TourDto> getAll() {
        return tourService.getAllTours();
    }

    @GetMapping("/name/{name}")
    public List<TourDto> getToursByName(@PathVariable String name) { return tourService.getToursByName(name); }


    @PostMapping
    public TourDto create(@Valid @RequestBody TourDto tour) {
        return tourService.createTour(tour);
    }

    @PutMapping("/{id}")
    public TourDto update(@PathVariable Long id, @Valid @RequestBody TourDto tourDto) {
            return tourService.updateTour(id, tourDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        tourService.deleteTour(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/report")
    public ResponseEntity<InputStreamResource> getTourReport(@PathVariable Long id) {
        try {
            TourEntity tour = tourService.getTourById(id);  // Ensure this method exists in TourService
            ByteArrayOutputStream pdfOutputStream = new ByteArrayOutputStream();
            pdfGenerator.generateTourReport(tour, pdfOutputStream);

            ByteArrayInputStream inputStream = new ByteArrayInputStream(pdfOutputStream.toByteArray());
            InputStreamResource resource = new InputStreamResource(inputStream);
            String filename = "tour_" + id + "_report.pdf";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", filename);

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(resource);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/report")
    public ResponseEntity<InputStreamResource> getToursSummaryReport() {
        try {
            List<TourEntity> tours = tourService.getAllTourEntities();  // Ensure this method exists in TourService
            ByteArrayOutputStream pdfOutputStream = new ByteArrayOutputStream();
            pdfGenerator.generateToursSummaryReport(tours, pdfOutputStream);

            ByteArrayInputStream inputStream = new ByteArrayInputStream(pdfOutputStream.toByteArray());
            InputStreamResource resource = new InputStreamResource(inputStream);
            String filename = "tours_summary_report.pdf";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", filename);

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(resource);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/export/csv")
    public void exportToursCsv(HttpServletResponse response) throws IOException {
        // Set response content type
        response.setContentType("text/csv");

        // Set response header for file attachment
        response.setHeader("Content-Disposition", "attachment; filename=\"tours.csv\"");

        response.setCharacterEncoding("UTF-8");
        // Get PrintWriter for writing CSV content
        PrintWriter writer =  new PrintWriter(new OutputStreamWriter(response.getOutputStream(), StandardCharsets.UTF_8));

        // Fetch all tours from the repository
        List<TourEntity> tours = tourService.getAllTourEntities();

        // Write tours data
        for (TourEntity tour : tours) {
            // Write tours section header
            writer.println("Tour");
            writer.println(
                    tour.getName() + ";;;" +
                    tour.getDescription() + ";;;" +
                    tour.getFrom() + ";;;" +
                    tour.getTo() + ";;;" +
                    tour.getTransportType() + ";;;" +
                    tour.getDistance() + ";;;" + // Convert meters to kilometers
                    tour.getEstimatedTime() + ";;;" + // Convert seconds to hours
                    tour.getImagePath()
            );

            // Write tour logs for the current tour
            writer.println("TourLogs");
            for (TourLogEntity log : tour.getTourLogs()) {
                writer.println(
                        log.getDate() + ";;;" +
                        log.getComment() + ";;;" +
                        log.getDifficulty() + ";;;" +
                        log.getTotalDistance() + ";;;" + // Convert meters to kilometers
                        log.getTotalTime() + ";;;" + // Convert seconds to hours
                        log.getRating()
                );
            }
        }

        // Flush and close the writer
        writer.flush();
        writer.close();
    }

    @GetMapping("/{id}/export/csv")
    public void exportTourCsv(HttpServletResponse response, @PathVariable Long id) throws IOException {
        TourEntity tour = tourService.getTourById(id);

        // Set response content type
        response.setContentType("text/csv");

        // Set response header for file attachment
        response.setHeader("Content-Disposition", "attachment; filename=\"tour_" + tour.getId() + ".csv\"");

        response.setCharacterEncoding("UTF-8");
        // Get PrintWriter for writing CSV content
        PrintWriter writer =  new PrintWriter(new OutputStreamWriter(response.getOutputStream(), StandardCharsets.UTF_8));

        // Write tours section header
        writer.println("Tour");
        writer.println(
            tour.getName() + ";;;" +
            tour.getDescription() + ";;;" +
            tour.getFrom() + ";;;" +
            tour.getTo() + ";;;" +
            tour.getTransportType() + ";;;" +
            tour.getDistance() + ";;;" + // Convert meters to kilometers
            tour.getEstimatedTime() + ";;;" + // Convert seconds to hours
            tour.getImagePath()
        );

        // Write tour logs for the current tour
        writer.println("TourLogs");
        for (TourLogEntity log : tour.getTourLogs()) {
            writer.println(
                log.getDate() + ";;;" +
                log.getComment() + ";;;" +
                log.getDifficulty() + ";;;" +
                log.getTotalDistance() + ";;;" + // Convert meters to kilometers
                log.getTotalTime() + ";;;" + // Convert seconds to hours
                log.getRating()
            );
        }

        // Flush and close the writer
        writer.flush();
        writer.close();
    }

    @PostMapping("/import/csv")
    public void importToursCsv(@RequestParam("file") MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        // Read the CSV file using BufferedReader
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            List<TourDto> tours = new ArrayList<>();
            List<TourLogDto> tourLogs = new ArrayList<>();
            boolean isTourSection = false;
            Long currentTourId = null;

            while ((line = reader.readLine()) != null) {
                if (line.equals("Tour")) {
                    isTourSection = true;
                    continue;
                } else if (line.equals("TourLogs")) {
                    isTourSection = false;
                    continue;
                }

                if (isTourSection) {
                    // Parse tour data from CSV line and create a TourDto object
                    TourDto tourDto = parseTourFromCsvLine(line);
                    TourDto created = tourService.createTour(tourDto);
                    currentTourId = created.getId();
                } else {
                    // Parse tour log data from CSV line and create a TourLogDto object
                    TourLogDto tourLogDto = parseTourLogFromCsvLine(line);
                    tourLogService.createTourLog(tourLogDto, currentTourId);
                }
            }
        }
    }

    // Helper method to parse tour data from a CSV line
    private TourDto parseTourFromCsvLine(String line) {
        String[] parts = line.split(";;;");

        TourDto tourDto = new TourDto();
        tourDto.setName(parts[0]);
        tourDto.setDescription(parts[1]);
        tourDto.setFrom(parts[2]);
        tourDto.setTo(parts[3]);
        tourDto.setTransportType(parts[4]);
        tourDto.setDistance(Double.parseDouble(parts[5]) * 1000); // Convert kilometers to meters
        tourDto.setEstimatedTime(Double.parseDouble(parts[6]) * 3600); // Convert hours to seconds
        tourDto.setImagePath(parts[7]);

        return tourDto;
    }

    // Helper method to parse tour log data from a CSV line
    private TourLogDto parseTourLogFromCsvLine(String line) {
        String[] parts = line.split(";;;");

        TourLogDto tourLogDto = new TourLogDto();
        tourLogDto.setDate(parseDate(parts[0]));
        tourLogDto.setComment(parts[1]);
        tourLogDto.setDifficulty(Integer.parseInt(parts[2]));
        tourLogDto.setTotalDistance(Double.parseDouble(parts[3])); // Convert kilometers to meters
        tourLogDto.setTotalTime(Double.parseDouble(parts[4])); // Convert hours to seconds
        tourLogDto.setRating(Integer.parseInt(parts[5]));

        return tourLogDto;
    }

    // Helper method to parse date from string
    private Date parseDate(String dateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // Adjust the format according to your date string format
        try {
            return dateFormat.parse(dateString);
        } catch (ParseException e) {
            // Handle parsing exception
            return null;
        }
    }


    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}
