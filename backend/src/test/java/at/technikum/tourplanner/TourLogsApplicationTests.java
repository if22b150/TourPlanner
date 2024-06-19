package at.technikum.tourplanner;

import at.technikum.tourplanner.controller.TourController;
import at.technikum.tourplanner.controller.TourLogController;
import at.technikum.tourplanner.service.TourLogService;
import at.technikum.tourplanner.service.TourService;
import at.technikum.tourplanner.service.dto.TourDto;
import at.technikum.tourplanner.service.dto.TourLogDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import jakarta.annotation.PostConstruct;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.*;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = TourLogController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class TourLogsApplicationTests {
    @MockBean
    private TourLogService tourLogService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private ObjectWriter objectWriter;

    private TourDto tour = TourDto.builder()
            .id(1L)
            .name("updatedTest")
            .description("Test description 1")
            .from("Test origin 1")
            .to("Test destination 1")
            .transportType("Test transport type 1")
            .distance(100.0)
            .estimatedTime(1.5)
            .imagePath("test_image1.jpg")
            .build();;
    private TourLogDto tourLog1 = TourLogDto.builder()
            .id(1L)
            .date(new Date(System.currentTimeMillis()))
            .comment("test1")
            .difficulty(5)
            .totalDistance(2.0)
            .totalTime(2.0)
            .rating(3)
            .tour(tour)
            .build();

    private TourLogDto tourLog2 = TourLogDto.builder()
            .id(2L)
            .date(new Date(System.currentTimeMillis()))
            .comment("test2")
            .difficulty(15)
            .totalDistance(2.0)
            .totalTime(3.0)
            .rating(4)
            .tour(tour)
            .build();

    private TourLogDto updatedTourLog = TourLogDto.builder()
            .id(1L)
            .date(new Date(System.currentTimeMillis()))
            .comment("test3")
            .difficulty(2)
            .totalDistance(2.0)
            .totalTime(1.0)
            .rating(5)
            .tour(tour)
            .build();

    @PostConstruct
    public void setUp() {
        this.objectWriter = objectMapper.writer();
    }

    @Test
    void getAllTourLogsByTourId_success() throws Exception {
        List<TourLogDto> tourLogs = new ArrayList<>(Arrays.asList(tourLog1, tourLog2));

        Mockito.when(tourLogService.getAllTourLogsByTour(tour.getId())).thenReturn(tourLogs);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/tours/"+ tour.getId() +"/tour-logs")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2)));
    }

    @Test
    void getAllTourLogsByTourId_failed() throws Exception {
        List<TourLogDto> tourLogs = new ArrayList<>(Collections.emptyList());

        Mockito.when(tourLogService.getAllTourLogsByTour(tour.getId())).thenReturn(tourLogs);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/tours/"+ tour.getId() +"/tour-logs")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(0)));
    }

    @Test
    void createTour_success() throws Exception {
        Mockito.when(tourLogService.createTourLog(tourLog1, tour.getId())).thenReturn(tourLog1);

        String content = objectWriter.writeValueAsString(tourLog1);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/tours/"+ tour.getId() +"/tour-logs")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(content);

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()));
    }

    @Test
    void createTour_failed() throws Exception {
        Mockito.when(tourLogService.createTourLog(tourLog1, tour.getId())).thenReturn(null);

        String content = objectWriter.writeValueAsString(tourLog1);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/tours/"+ tour.getId() +"/tour-logs")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(content);

        mockMvc.perform(mockRequest)
                .andExpect(jsonPath("$").doesNotExist());
    }

    @Test
    void updateTour_success() throws Exception {
        Mockito.when(tourLogService.updateTourLog(tour.getId(), tourLog1.getId(), updatedTourLog)).thenReturn(updatedTourLog);

        String updatedContent = objectWriter.writeValueAsString(updatedTourLog);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/tours/"+ tour.getId() +"/tour-logs/" + tourLog1.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(updatedContent);

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.comment", is("test3")));
    }

    @Test
    void updateTour_failed() throws Exception {
        Mockito.when(tourLogService.updateTourLog(tour.getId(), tourLog1.getId(), updatedTourLog)).thenReturn(null);

        String updatedContent = objectWriter.writeValueAsString(updatedTourLog);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/tours/"+ tour.getId() +"/tour-logs/" + tourLog1.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(updatedContent);

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").doesNotExist());
    }

    @Test
    void getTourLogById_success() throws Exception {
        Mockito.when(tourLogService.getTourLogById(tour.getId(), tourLog1.getId())).thenReturn(tourLog1);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/tours/"+ tour.getId() +"/tour-logs/" + tourLog1.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()));
    }

    @Test
    void getTourLogById_failed() throws Exception {
        Mockito.when(tourLogService.getTourLogById(tour.getId(), tourLog1.getId())).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/tours/"+ tour.getId() +"/tour-logs/" + tourLog1.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").doesNotExist());
    }

    @Test
    void deleteTourById_success() throws Exception {
        Mockito.when(tourLogService.getTourLogById(tour.getId(), tourLog1.getId())).thenReturn(tourLog1);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/tours/"+ tour.getId() +"/tour-logs/" + tourLog1.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void deleteTourById_failed() throws Exception {
        Mockito.when(tourLogService.getTourLogById(tour.getId(), tourLog1.getId())).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/tours/"+ tour.getId() +"/tour-logs/" + tourLog1.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").doesNotExist());
    }
}
