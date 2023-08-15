package com.hbtheme.infigestback.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hbtheme.infigestback.dto.StateRegisteredNurseRequest;
import com.hbtheme.infigestback.dto.StateRegisteredNurseResponse;
import com.hbtheme.infigestback.dto.StateRegisteredNurseSimpleResponse;
import com.hbtheme.infigestback.mapper.StateRegisteredNurseMapper;
import com.hbtheme.infigestback.model.StateRegisteredNurse;
import com.hbtheme.infigestback.service.StateRegisteredNurseService;
import com.hbtheme.infigestback.tools.Constant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = StateRegisteredNurseController.class)
@ActiveProfiles("test")
class StateRegisteredNurseControllerTest {

	@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
	@Autowired
	private MockMvc mockMvc;

	private StateRegisteredNurseRequest stateRegisteredNurseRequest;
	private final List<StateRegisteredNurse> stateRegisteredNurses = new ArrayList<>();
	private List<StateRegisteredNurseSimpleResponse> stateRegisteredNurseSimpleResponses = new ArrayList<>();
	
	@MockBean
	private StateRegisteredNurseMapper stateRegisteredNurseMapper;

	@MockBean
	private StateRegisteredNurseService stateRegisteredNurseService;

	@BeforeEach
	void setUp() {
		this.stateRegisteredNurseRequest = StateRegisteredNurseRequest.builder().id(2L).firstName("John")
				.lastName("Doe").build();
		this.stateRegisteredNurses.add(StateRegisteredNurse.builder().id(stateRegisteredNurseRequest.getId())
				.firstName(stateRegisteredNurseRequest.getFirstName())
				.lastName(stateRegisteredNurseRequest.getLastName()).build());
		this.stateRegisteredNurseSimpleResponses = this.stateRegisteredNurses.stream().map(srn-> stateRegisteredNurseMapper.toSimpleResponse(srn)).toList();
	}

	@Test
	void createStateRegisteredNurse_success() throws Exception {
		// GIVEN
		doNothing().when(stateRegisteredNurseService)
				.saveStateRegisteredNurse(Mockito.any(StateRegisteredNurseRequest.class), Mockito.anyBoolean());

		// WHEN THEN
		this.mockMvc
				.perform(post(Constant.NURSE_RESOURCE_API).contentType(MediaType.APPLICATION_JSON)
						.content(asJsonString(stateRegisteredNurseRequest)).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated()).andExpect(content().string("Nurse created successfully!"));

	}

	@Test
	void createStateRegisteredNurse_shouldFailOnExceptionDetected() throws Exception {
		// GIVEN
		doThrow(IllegalArgumentException.class).when(stateRegisteredNurseService)
				.saveStateRegisteredNurse(stateRegisteredNurseRequest, false);

		// WHEN THEN
		this.mockMvc
				.perform(post(Constant.NURSE_RESOURCE_API).contentType(MediaType.APPLICATION_JSON)
						.content(asJsonString(stateRegisteredNurseRequest)).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(content().string("Error while processing Nurse creation"));

	}

	@Test
	void findAllStateRegisteredNurses_success() throws Exception {
		// GIVEN
		ResponseEntity<StateRegisteredNurseResponse> expectedResponse = new ResponseEntity<>(
				StateRegisteredNurseResponse.builder()
						.message(String.format("Successfully returned %s Nurse(s)", stateRegisteredNurses.size()))
						.stateRegisteredNurses(stateRegisteredNurseSimpleResponses).build(),
				HttpStatus.OK);
		when(stateRegisteredNurseService.findAllStateRegisteredNurses()).thenReturn(stateRegisteredNurses);

		// WHEN // THEN
		this.mockMvc.perform(get(Constant.NURSE_RESOURCE_API)).andExpect(status().isOk())
				.andExpect(content().string(asJsonString(expectedResponse.getBody())));
	}

	@Test
	void findStateRegisteredNursesById_success() throws Exception {
		// GIVEN
		Long nurseId = 42L;
		ResponseEntity<StateRegisteredNurseResponse> expectedResponse = new ResponseEntity<>(
				StateRegisteredNurseResponse.builder().message("Successfully returned 1 Nurse")
						.stateRegisteredNurses(stateRegisteredNurseSimpleResponses).build(),
				HttpStatus.OK);
		when(stateRegisteredNurseService.findStateRegisteredNurseById(nurseId)).thenReturn(stateRegisteredNurses);

		// WHEN // THEN
		this.mockMvc.perform(get(Constant.NURSE_RESOURCE_API + "/byId/{id}", nurseId)).andExpect(status().isOk())
				.andExpect(content().string(asJsonString(expectedResponse.getBody())));
	}

	@Test
	void findStateRegisteredNursesById_shouldFailOnMissmatchingParam() throws Exception {
		// WHEN // THEN
		this.mockMvc.perform(get(Constant.NURSE_RESOURCE_API + "/byId/{id}", any(String.class)))
				.andExpect(status().isInternalServerError()).andExpect(content().string("Incorrect field type!"));
	}

	@Test
	void updateStateRegisteredNurseById_success() throws Exception {
		// GIVEN
		doNothing().when(stateRegisteredNurseService)
				.saveStateRegisteredNurse(Mockito.any(StateRegisteredNurseRequest.class), Mockito.anyBoolean());

		// WHEN THEN
		this.mockMvc
				.perform(put(Constant.NURSE_RESOURCE_API + "/update").contentType(MediaType.APPLICATION_JSON)
						.content(asJsonString(stateRegisteredNurseRequest)).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(content().string("Nurse updated successfully!"));
	}

	@Test
	void deleteStateRegisteredNurseById_success() throws Exception {
		// GIVEN
		doNothing().when(stateRegisteredNurseService).deleteStateRegisteredNurseById(Mockito.any(Long.class));

		// WHEN // THEN
		this.mockMvc.perform(delete(Constant.NURSE_RESOURCE_API + "/delete/{id}", Mockito.any(Long.class)))
				.andExpect(status().isOk()).andExpect(content().string("Nurse deleted successfully!"));
	}

	@Test
	void deleteStateRegisteredNurseById_shouldFailOnMismatchParameterType() throws Exception {
		// WHEN // THEN
		this.mockMvc.perform(delete(Constant.NURSE_RESOURCE_API + "/delete/{id}", "toto"))
				.andExpect(status().isInternalServerError()).andExpect(content().string("Incorrect field type!"));
	}

	public static String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
