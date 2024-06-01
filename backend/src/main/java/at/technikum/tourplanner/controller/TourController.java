package at.technikum.tourplanner.controller;

import at.technikum.tourplanner.persistence.entity.TourEntity;
import at.technikum.tourplanner.service.dto.TourDto;
import at.technikum.tourplanner.service.TourService;
import at.technikum.tourplanner.service.reports.PdfGenerator;
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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "tours")
public class TourController {
    @Autowired
    private TourService tourService;

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
