package com.hbtheme.infigestback.controller;

import com.hbtheme.infigestback.dto.CustomerInvoiceResponse;
import com.hbtheme.infigestback.dto.InsuranceRequest;
import com.hbtheme.infigestback.dto.InsuranceResponse;
import com.hbtheme.infigestback.dto.InsuranceSimpleResponse;
import com.hbtheme.infigestback.mapper.InsuranceMapper;
import com.hbtheme.infigestback.model.Insurance;
import com.hbtheme.infigestback.service.InsuranceService;
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
@WebMvcTest(controllers = InsuranceController.class)
@ActiveProfiles("test")
class InsuranceControllerTest {
	
	private InsuranceRequest insuranceRequest;
	private final List<InsuranceSimpleResponse> insuranceSimpleResponses = new ArrayList<>();
	private final List<Insurance> insurances = new ArrayList<>();

	@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private InsuranceService insuranceService;

	@MockBean
	private InsuranceMapper insuranceMapper;
	
	@BeforeEach
	void setUp() {
		this.insuranceRequest = InsuranceRequest.builder().id(2L)
				.name("Macif")
				.build();
	}

	@Test
	void createInvoice_success() throws Exception {
		// GIVEN
		ResponseEntity<InsuranceResponse> expectedResponse = new ResponseEntity<>(
				new InsuranceResponse(
						String.format("Insurance with name \"%s\" created successfully!", this.insuranceRequest.getName())),
				HttpStatus.CREATED);
		doNothing().when(insuranceService).saveInsurance(Mockito.any(InsuranceRequest.class), Mockito.anyBoolean());

		// WHEN THEN
		this.mockMvc
				.perform(post(Constant.INSURANCE_RESOURCE_API).contentType(MediaType.APPLICATION_JSON)
						.content(Parser.asJsonString(insuranceRequest)).accept(MediaType.APPLICATION_JSON))
				.andExpect(content().string(Parser.asJsonString(expectedResponse.getBody())));

	}
	
	@Test
	void createInsuranceFailOnExceptionDetected() throws Exception {
		// GIVEN
		doThrow(IllegalArgumentException.class).when(insuranceService).saveInsurance(insuranceRequest, false);

		// WHEN THEN
		this.mockMvc
				.perform(post(Constant.INSURANCE_RESOURCE_API).contentType(MediaType.APPLICATION_JSON)
						.content(Parser.asJsonString(insuranceRequest)).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(content().string("Error while processing insurance creation"));

	}
	
	@Test
	void findAllInsurance() throws Exception {
		// GIVEN
		ResponseEntity<InsuranceResponse> expectedResponse = new ResponseEntity<>(InsuranceResponse.builder()
				.message(String.format("Successfully returned %s insurance(s)", insuranceSimpleResponses.size()))
				.insuranceSimpleResponses(insuranceSimpleResponses).build(), HttpStatus.OK);
		
		when(insuranceService.findAllInsurances()).thenReturn(insurances);

		// WHEN // THEN
		this.mockMvc.perform(get(Constant.INSURANCE_RESOURCE_API)).andExpect(status().isOk())
				.andExpect(content().string(Parser.asJsonString(expectedResponse.getBody())));
	}
	
	@Test
	void findInsuranceById_success() throws Exception {
		// GIVEN
		ResponseEntity<InsuranceResponse> expectedResponse = new ResponseEntity<>(InsuranceResponse.builder()
				.message("Successfully returned 1 insurance")
				.insuranceSimpleResponses(insuranceSimpleResponses).build(), HttpStatus.OK);
		
	    when(insuranceService.findInsuranceById(Mockito.anyLong())).thenReturn(insurances);

		// WHEN // THEN
		this.mockMvc.perform(get(Constant.INSURANCE_RESOURCE_API + "/byId/{id}", this.insuranceRequest.getId())).andExpect(status().isOk())
				.andExpect(content().string(Parser.asJsonString(expectedResponse.getBody())));
	}
	
	@Test
	void findInsuranceById_shouldFailOnMissmatchingParam() throws Exception {
		// WHEN // THEN
		this.mockMvc.perform(get(Constant.INSURANCE_RESOURCE_API + "/byId/{id}", any(String.class)))
				.andExpect(status().isInternalServerError()).andExpect(content().string("Incorrect field type!"));
	}
	
	@Test
	void updateInsurance_success() throws Exception {
		// GIVEN
		doNothing().when(insuranceService).saveInsurance(Mockito.any(InsuranceRequest.class), Mockito.anyBoolean());

		// WHEN THEN
		this.mockMvc
				.perform(put(Constant.INSURANCE_RESOURCE_API + "/update").contentType(MediaType.APPLICATION_JSON)
						.content(Parser.asJsonString(insuranceRequest)).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(content().string("Insurance updated successfully!"));
	}
	
	@Test
	void deleteInsuranceById_success() throws Exception {
		// GIVEN
		doNothing().when(insuranceService).deleteInsuranceById(Mockito.any(Long.class));

		// WHEN // THEN
		this.mockMvc.perform(delete(Constant.INSURANCE_RESOURCE_API + "/delete/{id}", Mockito.any(Long.class)))
				.andExpect(status().isOk()).andExpect(content().string("Insurance deleted successfully!"));
	}
	
	@Test
	void deleteInsuranceById_shouldFailOnMismatchParameterType() throws Exception {
		// WHEN // THEN
		this.mockMvc.perform(delete(Constant.INSURANCE_RESOURCE_API + "/delete/{id}", "toto"))
				.andExpect(status().isInternalServerError()).andExpect(content().string("Incorrect field type!"));
	}
	
}
