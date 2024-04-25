package at.technikum.tourplanner.controller;

import at.technikum.tourplanner.service.TourLogService;
import at.technikum.tourplanner.service.dto.TourLogDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "tours/{tourId}/tour-logs")
public class TourLogController {
    @Autowired
    private TourLogService tourLogService;

    @GetMapping
    public List<TourLogDto> getAll(@PathVariable Long tourId) {
        return tourLogService.getAllTourLogsByTour(tourId);
    }

    @PostMapping
    public TourLogDto create(@PathVariable Long tourId, @Valid @RequestBody TourLogDto tourLog) {
        return tourLogService.createTourLog(tourLog, tourId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        tourLogService.deleteTourLog(id);
        return ResponseEntity.noContent().build();
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
