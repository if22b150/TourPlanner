package at.technikum.tourplanner.service.impl;

import at.technikum.tourplanner.persistence.entity.TourLogEntity;
import at.technikum.tourplanner.persistence.repository.TourLogRepository;
import at.technikum.tourplanner.service.EmailService;
import at.technikum.tourplanner.service.MapApi;
import at.technikum.tourplanner.service.TourLogService;
import at.technikum.tourplanner.service.dto.TourDto;
import at.technikum.tourplanner.persistence.entity.TourEntity;
import at.technikum.tourplanner.service.dto.TourLogDto;
import at.technikum.tourplanner.service.helper.MailGenerator;
import at.technikum.tourplanner.service.mapper.TourMapper;
import at.technikum.tourplanner.persistence.repository.TourRepository;
import at.technikum.tourplanner.service.TourService;
import at.technikum.tourplanner.service.helper.PdfGenerator;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
@Transactional
public class TourServiceImpl implements TourService {
    @Autowired
    private TourRepository tourRepository;
    @Autowired
    private TourMapper tourMapper;
    @Autowired
    private TourLogRepository tourLogRepository;
    @Autowired
    private MapApi mapApi;
    @Autowired
    private PdfGenerator pdfGenerator;
    @Autowired
    private TourLogService tourLogService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private MailGenerator mailGenerator;

    @Override
    public TourDto createTour(TourDto tourDto) {
        String startCoordinate = mapApi.searchAddress(tourDto.getFrom());
        if (startCoordinate == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "From location not found");
        }
        String endCoordinate = mapApi.searchAddress(tourDto.getTo());
        if (endCoordinate == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "To location not found");
        }

        RouteInfo route = mapApi.searchDirection(startCoordinate, endCoordinate, tourDto.getTransportType());

        TourEntity entity = TourEntity.builder()
                .name(tourDto.getName())
                .description(tourDto.getDescription())
                .from(tourDto.getFrom())
                .to(tourDto.getTo())
                .distance(route.getDistance())
                .estimatedTime(route.getDuration())
                .transportType(tourDto.getTransportType())
                .imagePath(tourDto.getImagePath())
                .build();
        tourRepository.save(entity);

        // TODO: the image

        return tourMapper.mapToDto(entity);
    }

    @Override
    public List<TourDto> getAllTours() {
        return tourMapper.mapToDto(tourRepository.findAllByOrderByIdAsc());
    }

    @Override
    public List<TourDto> getToursByName(String name) {
        return tourMapper.mapToDto(tourRepository.findByNameIgnoreCase(name));
    }

    @Override
    public TourDto updateTour(Long id, TourDto tourDto) {
        TourEntity tourEntity = tourRepository.getReferenceById(id);

        tourEntity.setName(tourDto.getName());
        tourEntity.setDescription(tourDto.getDescription());
        tourEntity.setFrom(tourDto.getFrom());
        tourEntity.setTo(tourDto.getTo());
        tourEntity.setDistance(tourDto.getDistance());
        tourEntity.setEstimatedTime(tourDto.getEstimatedTime());
        tourEntity.setTransportType(tourDto.getTransportType());
        tourEntity.setImagePath(tourDto.getImagePath());

        // Save the updated entity
        tourRepository.save(tourEntity);

        return tourMapper.mapToDto(tourEntity);
    }

    @Override
    public void deleteTour(Long id) {
        TourEntity tourEntity = tourRepository.getReferenceById(id);
        // Delete associated TourLogs
        tourLogRepository.deleteByTour(tourEntity);
        // Delete the Tour
        tourRepository.delete(tourEntity);
    }

    @Override
    public TourEntity getTourById(Long id) {
        Optional<TourEntity> optionalTour = tourRepository.findById(id);
        if (optionalTour.isPresent()) {
            return optionalTour.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Tour not found");
        }
    }

    @Override
    public List<TourEntity> getAllTourEntities() {
        return tourRepository.findAll();
    }

    @Override
    public ResponseEntity<InputStreamResource> generateTourReport(Long id) {
        try {
            TourEntity tour = getTourById(id);
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

    @Override
    public ResponseEntity<InputStreamResource> generateToursSummaryReport() {
        try {
            List<TourEntity> tours = getAllTourEntities();
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

    @Override
    public void exportToursCsv(HttpServletResponse response) throws IOException {
        // Set response content type
        response.setContentType("text/csv");

        // Set response header for file attachment
        response.setHeader("Content-Disposition", "attachment; filename=\"tours.csv\"");

        response.setCharacterEncoding("UTF-8");
        // Get PrintWriter for writing CSV content
        PrintWriter writer =  new PrintWriter(new OutputStreamWriter(response.getOutputStream(), StandardCharsets.UTF_8));

        // Fetch all tours from the repository
        List<TourEntity> tours = this.getAllTourEntities();

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

    @Override
    public void exportTourCsv(HttpServletResponse response, Long id) throws IOException {
        TourEntity tour = this.getTourById(id);

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

    @Override
    public void importToursCsv(MultipartFile file) throws IOException {
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
                    TourDto created = this.createTour(tourDto);
                    currentTourId = created.getId();
                } else {
                    // Parse tour log data from CSV line and create a TourLogDto object
                    TourLogDto tourLogDto = parseTourLogFromCsvLine(line);
                    tourLogService.createTourLog(tourLogDto, currentTourId);
                }
            }
        }
    }

    @Override
    public TourDto parseTourFromCsvLine(String line) {
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

    @Override
    public TourLogDto parseTourLogFromCsvLine(String line) {
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

    @Override
    public Date parseDate(String dateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // Adjust the format according to your date string format
        try {
            return dateFormat.parse(dateString);
        } catch (ParseException e) {
            // Handle parsing exception
            return null;
        }
    }

    @Override
    public ResponseEntity<Void> sendTour(Long id, String email) {
        TourEntity tour = getTourById(id);

        String body = mailGenerator.parseThymeleafTemplateMailTourDetails(tour);

        try {
            emailService.sendEmail(email, "Tour " + tour.getName(), body);
            return ResponseEntity.noContent().build();
        } catch (MessagingException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
