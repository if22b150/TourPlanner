package at.technikum.tourplanner;

import at.technikum.tourplanner.controller.TourController;
import at.technikum.tourplanner.service.TourService;
import at.technikum.tourplanner.service.dto.TourDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import jakarta.annotation.PostConstruct;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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

	private ObjectWriter objectWriter;

	private TourDto tour1 = TourDto.builder()
			.id(1L)
			.name("Test")
			.description("Test description 1")
			.from("Test origin 1")
			.to("Test destination 1")
			.transportType("Test transport type 1")
			.distance(100.0)
			.estimatedTime(1.5)
			.imagePath("test_image1.jpg")
			.build();

	private TourDto tour2 = TourDto.builder()
			.id(2L)
			.name("Test Tour 2")
			.description("Test description 2")
			.from("Test origin 2")
			.to("Test destination 2")
			.transportType("Test transport type 2")
			.distance(200.0)
			.estimatedTime(2.5)
			.imagePath("test_image2.jpg")
			.build();

	private TourDto updatedTour = TourDto.builder()
			.id(1L)
			.name("updatedTest")
			.description("Test description 1")
			.from("Test origin 1")
			.to("Test destination 1")
			.transportType("Test transport type 1")
			.distance(100.0)
			.estimatedTime(1.5)
			.imagePath("test_image1.jpg")
			.build();


	@PostConstruct
	public void setUp() {
		this.objectWriter = objectMapper.writer();
	}

	@Test
	void createTour_success() throws Exception {
		Mockito.when(tourService.createTour(tour1)).thenReturn(tour1);

		String content = objectWriter.writeValueAsString(tour1);

		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/tours")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(content);

		mockMvc.perform(mockRequest)
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", notNullValue()));
	}

	@Test
	void createTour_failed() throws Exception {
		Mockito.when(tourService.createTour(tour1)).thenReturn(null);

		String content = objectWriter.writeValueAsString(tour1);

		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/tours")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(content);

		mockMvc.perform(mockRequest)
				.andExpect(jsonPath("$").doesNotExist());
	}

	@Test
	void updateTour_success() throws Exception {
		Mockito.when(tourService.getTourById(tour1.getId())).thenReturn(TourDto.toEntity(tour1));
		Mockito.when(tourService.updateTour(tour1.getId() ,updatedTour)).thenReturn(updatedTour);

		String updatedContent = objectWriter.writeValueAsString(updatedTour);

		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/tours/"+tour1.getId())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(updatedContent);

		mockMvc.perform(mockRequest)
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", notNullValue()))
				.andExpect(jsonPath("$.name", is("updatedTest")));
	}

	@Test
	void updateTour_failed() throws Exception {
		Mockito.when(tourService.getTourById(tour1.getId())).thenReturn(TourDto.toEntity(tour1));
		Mockito.when(tourService.updateTour(tour1.getId() ,updatedTour)).thenReturn(null);

		String updatedContent = objectWriter.writeValueAsString(updatedTour);

		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/tours/"+tour1.getId())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(updatedContent);

		mockMvc.perform(mockRequest)
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").doesNotExist());
	}

	@Test
	void deleteTourById_success() throws Exception {
		Mockito.when(tourService.getTourById(tour1.getId())).thenReturn(TourDto.toEntity(tour1));

		mockMvc.perform(MockMvcRequestBuilders
				.delete("/tours/"+tour1.getId())
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is2xxSuccessful());
	}

	@Test
	void deleteTourById_failed() throws Exception {
		Mockito.when(tourService.getTourById(tour1.getId())).thenReturn(null);

		mockMvc.perform(MockMvcRequestBuilders
						.delete("/tours/"+tour1.getId())
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$").doesNotExist());
	}

	@Test
	void getAllTours_success() throws Exception {
		List<TourDto> tours = new ArrayList<>(Arrays.asList(tour1, tour2));

		Mockito.when(tourService.getAllTours()).thenReturn(tours);

		mockMvc.perform(MockMvcRequestBuilders
				.get("/tours")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2)));
	}

	@Test
	void getAllTours_failure() throws Exception {
		Mockito.when(tourService.getAllTours()).thenReturn(Collections.emptyList());

		mockMvc.perform(MockMvcRequestBuilders
						.get("/tours")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(0)));
	}

	@Test
	void getTourById_success() throws Exception {
		Mockito.when(tourService.getTourById(tour1.getId())).thenReturn(TourDto.toEntity(tour1));

		mockMvc.perform(MockMvcRequestBuilders
				.get("/tours/"+tour1.getId())
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", notNullValue()));
	}

	@Test
	void getTourById_failure() throws Exception {
		Mockito.when(tourService.getTourById(tour1.getId())).thenReturn(null);

		mockMvc.perform(MockMvcRequestBuilders
						.get("/tours/"+tour1.getId())
						.contentType(MediaType.APPLICATION_JSON))
						.andExpect(jsonPath("$").doesNotExist());
	}

	@Test
	void getTourByName_success() throws Exception {
		List<TourDto> tours = new ArrayList<>(Arrays.asList(tour1, tour2));

		Mockito.when(tourService.getToursByName(tour1.getName())).thenReturn(tours);

		mockMvc.perform(MockMvcRequestBuilders
				.get("/tours/name/"+tour1.getName())
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", notNullValue()));
	}

	@Test
	void getTourByName_failure() throws Exception {
		Mockito.when(tourService.getToursByName(tour1.getName())).thenReturn(Collections.emptyList());

		mockMvc.perform(MockMvcRequestBuilders
						.get("/tours/name/"+tour1.getName())
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(0)));
	}

	@Test
	void getTourReport_success() throws Exception {
		InputStreamResource mockReport = new InputStreamResource(new ByteArrayInputStream("Mock report content".getBytes()));
		Mockito.when(tourService.generateTourReport(tour1.getId())).thenReturn(ResponseEntity.ok().body(mockReport));

		mockMvc.perform(MockMvcRequestBuilders
						.get("/tours/1/report")
						.contentType(MediaType.APPLICATION_PDF))
				.andExpect(status().isOk())
				.andExpect(content().string("Mock report content"));

	}

	@Test
	void getTourReport_failed() throws Exception {
		Mockito.when(tourService.generateTourReport(Mockito.anyLong())).thenReturn(ResponseEntity.notFound().build());

		mockMvc.perform(MockMvcRequestBuilders
						.get("/tours/1/report")
						.contentType(MediaType.APPLICATION_PDF))
				.andExpect(status().isNotFound());
	}
}
