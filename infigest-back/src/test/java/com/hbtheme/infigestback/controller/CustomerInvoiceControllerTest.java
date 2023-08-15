package com.hbtheme.infigestback.controller;

import com.hbtheme.infigestback.dto.CustomerInvoiceRequest;
import com.hbtheme.infigestback.dto.CustomerInvoiceResponse;
import com.hbtheme.infigestback.dto.CustomerInvoiceSimpleResponse;
import com.hbtheme.infigestback.mapper.CustomerInvoiceMapper;
import com.hbtheme.infigestback.model.CustomerInvoice;
import com.hbtheme.infigestback.service.CustomerInvoiceService;
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
import org.springframework.dao.DataIntegrityViolationException;
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
@WebMvcTest(controllers = CustomerInvoiceController.class)
@ActiveProfiles("test")
class CustomerInvoiceControllerTest {
	
	private CustomerInvoiceRequest customerInvoiceRequest;
	private final List<CustomerInvoice> customerInvoices = new ArrayList<>();
	private final List<CustomerInvoiceSimpleResponse> customerInvoiceSimpleResponses = new ArrayList<>();

	@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private CustomerInvoiceService customerInvoiceService;

	@MockBean
	private CustomerInvoiceMapper customerInvoiceMapper;
	
	@BeforeEach
	void setUp() {
		this.customerInvoiceRequest = CustomerInvoiceRequest.builder()
				.id(2L)
				.stateRegisteredNurseId(1L)
				.build();
	}

	@Test
	void createCustomerInvoice_success() throws Exception {
		// GIVEN
		ResponseEntity<CustomerInvoiceResponse> expectedResponse = new ResponseEntity<>(
				new CustomerInvoiceResponse("Customer invoice created successfully!"), HttpStatus.CREATED);
		doNothing().when(customerInvoiceService).saveCustomerInvoice(Mockito.any(CustomerInvoiceRequest.class), Mockito.anyBoolean());

		// WHEN THEN
		this.mockMvc
				.perform(post(Constant.CUSTOMER_INVOICE_RESOURCE_API).contentType(MediaType.APPLICATION_JSON)
						.content(Parser.asJsonString(customerInvoiceRequest)).accept(MediaType.APPLICATION_JSON))
				.andExpect(content().string(Parser.asJsonString(expectedResponse.getBody())));

	}
	
	@Test
	void createCustomerInvoice_shouldFailOnExceptionDetected() throws Exception {
		// GIVEN
		String exceptionMessage = "Custom error message";
		String errorResponseMessage = String.format("Error while processing customer invoice creation (%s)", exceptionMessage);
		doThrow(new IllegalArgumentException(exceptionMessage)).when(customerInvoiceService).saveCustomerInvoice(customerInvoiceRequest, false);
		ResponseEntity<CustomerInvoiceResponse> expectedResponse = new ResponseEntity<>(
				new CustomerInvoiceResponse(errorResponseMessage), HttpStatus.INTERNAL_SERVER_ERROR);

		// WHEN THEN
		this.mockMvc
				.perform(post(Constant.CUSTOMER_INVOICE_RESOURCE_API).contentType(MediaType.APPLICATION_JSON)
						.content(Parser.asJsonString(customerInvoiceRequest)).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(content().string(Parser.asJsonString(expectedResponse.getBody())));
	}
	
	@Test
	void findAllCustomerInvoices_success() throws Exception {
		// GIVEN
		ResponseEntity<CustomerInvoiceResponse> expectedResponse = new ResponseEntity<>(CustomerInvoiceResponse.builder()
				.message(String.format("Successfully returned %s customer invoice(s)", customerInvoiceSimpleResponses.size()))
				.customerInvoiceSimpleResponses(customerInvoiceSimpleResponses).build(), HttpStatus.OK);
		when(customerInvoiceService.findAllCustomerInvoices()).thenReturn(this.customerInvoices);

		// WHEN // THEN
		this.mockMvc.perform(get(Constant.CUSTOMER_INVOICE_RESOURCE_API)).andExpect(status().isOk())
				.andExpect(content().string(Parser.asJsonString(expectedResponse.getBody())));
	}

	@Test
	void findCustomerInvoiceById_success() throws Exception {
		// GIVEN
		ResponseEntity<CustomerInvoiceResponse> expectedResponse = new ResponseEntity<>(CustomerInvoiceResponse.builder()
				.message("Successfully returned 1 customer invoice")
				.customerInvoiceSimpleResponses(customerInvoiceSimpleResponses).build(), HttpStatus.OK);
		
	    when(customerInvoiceService.findCustomerInvoiceById(Mockito.anyLong())).thenReturn(customerInvoices);

		// WHEN // THEN
		this.mockMvc.perform(get(Constant.CUSTOMER_INVOICE_RESOURCE_API + "/byId/{id}",
						this.customerInvoiceRequest.getId())).andExpect(status().isOk())
				.andExpect(content().string(Parser.asJsonString(expectedResponse.getBody())));
	}

	@Test
	void findCustomerInvoiceById_shouldFailOnExceptionDetected() throws Exception {
		// GIVEN
		String exceptionMessage = "Custom error message";
		String errorResponseMessage = String.format("Failed to complete get invoice (%s)", exceptionMessage);
		doThrow(new IllegalArgumentException(exceptionMessage)).when(customerInvoiceService).findCustomerInvoiceById(anyLong());
		ResponseEntity<CustomerInvoiceResponse> expectedResponse = new ResponseEntity<>(
				new CustomerInvoiceResponse(errorResponseMessage), HttpStatus.INTERNAL_SERVER_ERROR);

		// WHEN THEN
		this.mockMvc.perform(get(Constant.CUSTOMER_INVOICE_RESOURCE_API + "/byId/{id}",
						this.customerInvoiceRequest.getId())).andExpect(status().isInternalServerError())
				.andExpect(content().string(Parser.asJsonString(expectedResponse.getBody())));
	}
	
	@Test
	void findCustomerInvoiceById_shouldFailOnMismatchingParam() throws Exception {
		// WHEN // THEN
		this.mockMvc.perform(get(Constant.CUSTOMER_INVOICE_RESOURCE_API + "/byId/{id}", any(String.class)))
				.andExpect(status().isInternalServerError()).andExpect(content().string("Incorrect field type!"));
	}
	
	@Test
	void updateCustomerInvoice_success() throws Exception {
		// GIVEN
		String successMessage = String.format("Customer invoice with id %s updated successfully!", this.customerInvoiceRequest.getId());
		ResponseEntity<CustomerInvoiceResponse> expectedResponse = new ResponseEntity<>(
				new CustomerInvoiceResponse(successMessage), HttpStatus.OK);
		doNothing().when(customerInvoiceService).saveCustomerInvoice(Mockito.any(CustomerInvoiceRequest.class), Mockito.anyBoolean());

		// WHEN THEN
		this.mockMvc
				.perform(put(Constant.CUSTOMER_INVOICE_RESOURCE_API + "/update").contentType(MediaType.APPLICATION_JSON)
						.content(Parser.asJsonString(customerInvoiceRequest)).accept(MediaType.APPLICATION_JSON))
				.andExpect(content().string(Parser.asJsonString(expectedResponse.getBody())));
	}

	@Test
	void updateCustomerInvoice_shouldFailOnExceptionDetected() throws Exception {
		// GIVEN
		String exceptionMessage = "Custom error message";
		String errorResponseMessage = String.format("Error while processing customer invoice updating (%s)", exceptionMessage);
		doThrow(new IllegalArgumentException(exceptionMessage)).when(customerInvoiceService).saveCustomerInvoice(customerInvoiceRequest, true);
		ResponseEntity<CustomerInvoiceResponse> expectedResponse = new ResponseEntity<>(
				new CustomerInvoiceResponse(errorResponseMessage), HttpStatus.INTERNAL_SERVER_ERROR);

		// WHEN THEN
		this.mockMvc
				.perform(put(Constant.CUSTOMER_INVOICE_RESOURCE_API + "/update").contentType(MediaType.APPLICATION_JSON)
						.content(Parser.asJsonString(customerInvoiceRequest)).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(content().string(Parser.asJsonString(expectedResponse.getBody())));
	}
	
	@Test
	void deleteCustomerInvoiceById_success() throws Exception {
		// GIVEN
		String successMessage = String.format("Customer invoice with id %s deleted successfully!", this.customerInvoiceRequest.getId());
		ResponseEntity<CustomerInvoiceResponse> expectedResponse = new ResponseEntity<>(
				new CustomerInvoiceResponse(successMessage), HttpStatus.OK);
		doNothing().when(customerInvoiceService).deleteCustomerInvoiceById(Mockito.any(Long.class));

		// WHEN // THEN
		this.mockMvc.perform(delete(Constant.CUSTOMER_INVOICE_RESOURCE_API + "/delete/{id}", this.customerInvoiceRequest.getId()))
				.andExpect(content().string(Parser.asJsonString(expectedResponse.getBody())));
	}

	@Test
	void deleteCustomerInvoiceById_shouldFailOnExceptionDetected() throws Exception {
		// GIVEN
		String exceptionMessage = "Custom error message";
		String errorResponseMessage = String.format("Error while processing customer invoice deleting (%s)", exceptionMessage);
		ResponseEntity<CustomerInvoiceResponse> expectedResponse = new ResponseEntity<>(
				new CustomerInvoiceResponse(errorResponseMessage), HttpStatus.INTERNAL_SERVER_ERROR);
		doThrow(new IllegalArgumentException(exceptionMessage)).when(customerInvoiceService).deleteCustomerInvoiceById(Mockito.any(Long.class));

		// WHEN THEN
		this.mockMvc
				.perform(delete(Constant.CUSTOMER_INVOICE_RESOURCE_API + "/delete/{id}", this.customerInvoiceRequest.getId()))
				.andExpect(status().isInternalServerError())
				.andExpect(content().string(Parser.asJsonString(expectedResponse.getBody())));
	}

	@Test
	void deleteCustomerInvoiceById_shouldFailOnDataIntegrityViolationException() throws Exception {
		// GIVEN
		String exceptionMessage = "Custom error message";
		String errorResponseMessage = String.format("You cannot delete this customer invoice as he has already been assigned (%s)", exceptionMessage);
		ResponseEntity<CustomerInvoiceResponse> expectedResponse = new ResponseEntity<>(
				new CustomerInvoiceResponse(errorResponseMessage), HttpStatus.INTERNAL_SERVER_ERROR);
		doThrow(new DataIntegrityViolationException(exceptionMessage)).when(customerInvoiceService).deleteCustomerInvoiceById(Mockito.any(Long.class));

		// WHEN THEN
		this.mockMvc
				.perform(delete(Constant.CUSTOMER_INVOICE_RESOURCE_API + "/delete/{id}", this.customerInvoiceRequest.getId()))
				.andExpect(status().isInternalServerError())
				.andExpect(content().string(Parser.asJsonString(expectedResponse.getBody())));
	}
	
	@Test
	void deleteCustomerInvoiceById_shouldFailOnMismatchParameterType() throws Exception {
		// WHEN // THEN
		this.mockMvc.perform(delete(Constant.CUSTOMER_INVOICE_RESOURCE_API + "/delete/{id}", "toto"))
				.andExpect(status().isInternalServerError()).andExpect(content().string("Incorrect field type!"));
	}
	
}
