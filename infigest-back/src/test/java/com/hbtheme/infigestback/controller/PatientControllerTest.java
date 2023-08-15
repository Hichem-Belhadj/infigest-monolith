package com.hbtheme.infigestback.controller;

import com.hbtheme.infigestback.dto.PatientRequest;
import com.hbtheme.infigestback.dto.PatientResponse;
import com.hbtheme.infigestback.dto.PatientSimpleResponse;
import com.hbtheme.infigestback.mapper.PatientMapper;
import com.hbtheme.infigestback.model.Patient;
import com.hbtheme.infigestback.service.PatientService;
import com.hbtheme.infigestback.tools.Constant;
import com.hbtheme.infigestback.tools.Parser;
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
@WebMvcTest(controllers = PatientController.class)
@ActiveProfiles("test")
class PatientControllerTest {

	private PatientRequest patientRequest;
	private final List<Patient> patients = new ArrayList<>();
	List<PatientSimpleResponse> patientSimpleResponses = new ArrayList<>();

	@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private PatientMapper patientMapper;

	@MockBean
	private PatientService patientService;

	@BeforeEach
	void setUp() {
		this.patientRequest = PatientRequest.builder().id(2L).firstName("John").lastName("Doe")
				.stateRegisteredNursesIds(List.of(1L)).build();
		Patient patient1 = Patient.builder()
				.id(42L)
				.firstName("John")
				.lastName("Doe")
				.build();
		this.patients.add(patient1);
		this.patientSimpleResponses = patients.stream().map(patient->patientMapper.toSimpleResponse(patient)).toList();
	}

	@Test
	void createPatient_success() throws Exception {
		// GIVEN
		doNothing().when(patientService).savePatient(Mockito.any(PatientRequest.class), Mockito.anyBoolean());

		// WHEN THEN
		this.mockMvc
				.perform(post(Constant.PATIENT_RESOURCE_API).contentType(MediaType.APPLICATION_JSON)
						.content(Parser.asJsonString(patientRequest)).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated()).andExpect(content().string("Patient created successfully!"));

	}
	
	@Test
	void createPatient_shouldFailOnExceptionDetected() throws Exception {
		// GIVEN
		doThrow(IllegalArgumentException.class).when(patientService).savePatient(patientRequest, false);

		// WHEN THEN
		this.mockMvc
				.perform(post(Constant.PATIENT_RESOURCE_API).contentType(MediaType.APPLICATION_JSON)
						.content(Parser.asJsonString(patientRequest)).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(content().string("Error while processing patient creation"));

	}
	
	@Test
	void findAllPatient_success() throws Exception {
		// GIVEN
		ResponseEntity<PatientResponse> expectedResponse = new ResponseEntity<>(PatientResponse.builder()
				.message(String.format("Successfully returned %s Patient(s)", patientSimpleResponses.size()))
				.patientSimpleResponses(patientSimpleResponses).build(), HttpStatus.OK);
		when(patientService.findAllPatients()).thenReturn(patients);

		// WHEN // THEN
		this.mockMvc.perform(get(Constant.PATIENT_RESOURCE_API)).andExpect(status().isOk())
				.andExpect(content().string(Parser.asJsonString(expectedResponse.getBody())));
	}

	@Test
	void findPatientById_success() throws Exception {
		// GIVEN
		ResponseEntity<PatientResponse> expectedResponse = new ResponseEntity<>(PatientResponse.builder()
				.message("Successfully returned 1 patient")
				.patientSimpleResponses(patientSimpleResponses).build(), HttpStatus.OK);
		
		when(patientService.findPatientById(this.patientRequest.getId())).thenReturn(patients);

		// WHEN // THEN
		this.mockMvc.perform(get(Constant.PATIENT_RESOURCE_API + "/byId/{id}", this.patientRequest.getId())).andExpect(status().isOk())
				.andExpect(content().string(Parser.asJsonString(expectedResponse.getBody())));
	}

	@Test
	void findPatientById_shouldFailOnMissmatchingParam() throws Exception {
		// WHEN // THEN
		this.mockMvc.perform(get(Constant.PATIENT_RESOURCE_API+ "/byId/{id}", any(String.class)))
				.andExpect(status().isInternalServerError()).andExpect(content().string("Incorrect field type!"));
	}

	@Test
	void updatePatient_success() throws Exception {
		// GIVEN
		doNothing().when(patientService).savePatient(Mockito.any(PatientRequest.class), Mockito.anyBoolean());

		// WHEN THEN
		this.mockMvc
				.perform(put("/api/v1/patient/update").contentType(MediaType.APPLICATION_JSON)
						.content(Parser.asJsonString(patientRequest)).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(content().string("Patient updated successfully!"));
	}

	@Test
	void deletePatientById_success() throws Exception {
		// GIVEN
		doNothing().when(patientService).deletePatientById(Mockito.any(Long.class));

		// WHEN // THEN
		this.mockMvc.perform(delete(Constant.PATIENT_RESOURCE_API + "/delete/{id}", Mockito.any(Long.class)))
				.andExpect(status().isOk()).andExpect(content().string("Patient deleted successfully!"));
	}

	@Test
	void deletePatientById_shouldFailOnMismatchParameterType() throws Exception {
		// WHEN // THEN
		this.mockMvc.perform(delete(Constant.PATIENT_RESOURCE_API + "/delete/{id}", "toto"))
				.andExpect(status().isInternalServerError()).andExpect(content().string("Incorrect field type!"));
	}

}
