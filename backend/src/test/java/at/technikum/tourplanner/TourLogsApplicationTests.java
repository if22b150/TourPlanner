package at.technikum.tourplanner;

import at.technikum.tourplanner.controller.TourController;
import at.technikum.tourplanner.controller.TourLogController;
import at.technikum.tourplanner.service.TourLogService;
import at.technikum.tourplanner.service.TourService;
import at.technikum.tourplanner.service.dto.TourDto;
import at.technikum.tourplanner.service.dto.TourLogDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Date;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

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

    private TourDto tour;
    private TourLogDto tourLogDto;
    private TourLogDto failedTourLog;

    @BeforeEach
    public void init() {
        tour = TourDto.builder()
                .id(1L)
                .name("Test Tour")
                .description("Test description")
                .from("Test origin")
                .to("Test destination")
                .transportType("Test transport type")
                .distance(100.0)
                .estimatedTime(2.5)
                .imagePath("test_image.jpg")
                .build();

        long currentTimeMillis = System.currentTimeMillis();

        tourLogDto = TourLogDto.builder()
                .id(1L)
                .date(new Date(currentTimeMillis))
                .comment("test")
                .difficulty(5)
                .totalDistance(2.0)
                .totalTime(2.0)
                .rating(3)
                .tour(tour)
                .build();


        failedTourLog = TourLogDto.builder()
                .id(2L)
                .build();
    }

    @Test
    void testCreateTourWorking() throws Exception {
        given(tourLogService.createTourLog(tourLogDto, tour.getId())).willAnswer((invocation -> invocation.getArgument(0)));

        ResultActions response = mockMvc.perform(post("/tours/" + tour.getId() + "/tour-logs")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tourLogDto))
        );

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.comment", CoreMatchers.is(tourLogDto.getComment())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalDistance", CoreMatchers.is(tourLogDto.getTotalDistance())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalTime", CoreMatchers.is(tourLogDto.getTotalTime())))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void testCreateTourFailing() throws Exception {
        given(tourLogService.createTourLog(tourLogDto, tour.getId())).willAnswer((invocation -> invocation.getArgument(0)));

        ResultActions response = mockMvc.perform(post("/tours/" + tour.getId() + "/tour-logs")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(failedTourLog))
        );

        response.andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andDo(MockMvcResultHandlers.print());
    }
}
