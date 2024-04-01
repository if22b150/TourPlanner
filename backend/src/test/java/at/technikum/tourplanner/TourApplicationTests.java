package at.technikum.tourplanner;

import at.technikum.tourplanner.controller.TourController;
import at.technikum.tourplanner.service.TourService;
import at.technikum.tourplanner.service.dto.TourDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
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

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(controllers = TourController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class TourApplicationTests {

	@MockBean
	private TourService tourService;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	private TourDto tour;
	private TourDto failedTour;

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

		failedTour = TourDto.builder()
				.id(2L)
				.build();
	}

	@Test
	void testCreateTourWorking() throws Exception {
		given(tourService.createTour(ArgumentMatchers.any())).willAnswer((invocation -> invocation.getArgument(0)));

		ResultActions response = mockMvc.perform(post("/tours")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(tour))
		);

		response.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is(tour.getName())))
				.andExpect(MockMvcResultMatchers.jsonPath("$.description", CoreMatchers.is(tour.getDescription())))
				.andExpect(MockMvcResultMatchers.jsonPath("$.to", CoreMatchers.is(tour.getTo())))
				.andExpect(MockMvcResultMatchers.jsonPath("$.from", CoreMatchers.is(tour.getFrom())))
				.andDo(MockMvcResultHandlers.print());
	}

	@Test
	void testCreateTourFailing() throws Exception {
		given(tourService.createTour(ArgumentMatchers.any())).willAnswer((invocation -> invocation.getArgument(0)));

		ResultActions response = mockMvc.perform(post("/tours")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(failedTour))
		);

		response.andExpect(MockMvcResultMatchers.status().is4xxClientError())
				.andDo(MockMvcResultHandlers.print());
	}


	@Test
	void testgetAllTours() throws Exception {
		ResultActions response = mockMvc.perform(get("/tours")
				.contentType(MediaType.APPLICATION_JSON));

		response.andExpect(MockMvcResultMatchers.status().isOk());
	}
}
