package at.technikum.tourplanner.service;

import at.technikum.tourplanner.persistence.entity.TourEntity;
import at.technikum.tourplanner.service.dto.TourDto;
import at.technikum.tourplanner.service.dto.TourLogDto;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;

public interface TourService {
    TourEntity getTourById(Long id);
    TourDto createTour(TourDto tourDto);
    List<TourDto> getAllTours();

    List<TourEntity> getAllTourEntities();

    List<TourDto> getToursByName(String name);

    TourDto updateTour(Long id, TourDto tourDto);

    void deleteTour(Long id);

    ResponseEntity<InputStreamResource> generateTourReport(Long id);

    ResponseEntity<InputStreamResource> generateToursSummaryReport();

    void exportToursCsv(HttpServletResponse response) throws IOException;

    void exportTourCsv(HttpServletResponse response, Long id) throws IOException;

    void importToursCsv(MultipartFile file) throws IOException;

    TourDto parseTourFromCsvLine(String line);

    TourLogDto parseTourLogFromCsvLine(String line);

    Date parseDate(String dateString);

    ResponseEntity<Void> sendTour(Long id, String email);
}
