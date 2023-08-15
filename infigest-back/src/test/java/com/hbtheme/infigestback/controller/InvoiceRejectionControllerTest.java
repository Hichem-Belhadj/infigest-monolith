package com.hbtheme.infigestback.controller;

import com.hbtheme.infigestback.dto.InvoiceRejectionRequest;
import com.hbtheme.infigestback.dto.InvoiceRejectionResponse;
import com.hbtheme.infigestback.dto.InvoiceRejectionSimpleResponse;
import com.hbtheme.infigestback.model.InvoiceRejection;
import com.hbtheme.infigestback.service.InvoiceRejectionService;
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
@WebMvcTest(controllers = InvoiceRejectionController.class)
@ActiveProfiles("test")
class InvoiceRejectionControllerTest {
	
	private InvoiceRejectionRequest invoiceRejectionRequest;
	private final List<InvoiceRejection> invoiceRejections = new ArrayList<>();
	private final List<InvoiceRejectionSimpleResponse> invoiceRejectionSimpleResponses = new ArrayList<>();

	@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private InvoiceRejectionService invoiceRejectionService;

	@BeforeEach
	void setUp() {
		this.invoiceRejectionRequest = InvoiceRejectionRequest.builder().id(2L)
				.stateRegisteredNurseId(1L)
				.patientId(2L)
				.build();
	}

	@Test
	void createInvoiceRejection_success() throws Exception {
		// GIVEN
		doNothing().when(invoiceRejectionService).saveInvoiceRejection(Mockito.any(InvoiceRejectionRequest.class), Mockito.anyBoolean());

		// WHEN THEN
		this.mockMvc
				.perform(post(Constant.INVOICE_REJECTION_RESOURCE_API).contentType(MediaType.APPLICATION_JSON)
						.content(Parser.asJsonString(invoiceRejectionRequest)).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated()).andExpect(content().string("Invoice rejection created successfully!"));

	}
	
	@Test
	void createInvoiceRejection_shouldFailOnExceptionDetected() throws Exception {
		// GIVEN
		doThrow(IllegalArgumentException.class).when(invoiceRejectionService).saveInvoiceRejection(invoiceRejectionRequest, false);

		// WHEN THEN
		this.mockMvc
				.perform(post(Constant.INVOICE_REJECTION_RESOURCE_API).contentType(MediaType.APPLICATION_JSON)
						.content(Parser.asJsonString(invoiceRejectionRequest)).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(content().string("Error while processing invoice rejection creation"));

	}

	@Test
	void findAllInvoicesRejection_success() throws Exception {
		// GIVEN
		ResponseEntity<InvoiceRejectionResponse> expectedResponse = new ResponseEntity<>(InvoiceRejectionResponse.builder()
				.message(String.format("Successfully returned %s invoice rejection(s)", invoiceRejectionSimpleResponses.size()))
				.invoiceRejectionSimpleResponses(invoiceRejectionSimpleResponses).build(), HttpStatus.OK);
		when(invoiceRejectionService.findAllInvoiceRejections()).thenReturn(invoiceRejections);

		// WHEN // THEN
		this.mockMvc.perform(get(Constant.INVOICE_REJECTION_RESOURCE_API)).andExpect(status().isOk())
				.andExpect(content().string(Parser.asJsonString(expectedResponse.getBody())));
	}

	@Test
	void findInvoiceRejectionById_success() throws Exception {
		// GIVEN
		ResponseEntity<InvoiceRejectionResponse> expectedResponse = new ResponseEntity<>(InvoiceRejectionResponse.builder()
				.message("Successfully returned 1 invoice rejection")
				.invoiceRejectionSimpleResponses(invoiceRejectionSimpleResponses).build(), HttpStatus.OK);
		
	    when(invoiceRejectionService.findInvoiceRejectionById(Mockito.anyLong())).thenReturn(invoiceRejections);

		// WHEN // THEN
		this.mockMvc.perform(get(Constant.INVOICE_REJECTION_RESOURCE_API + "/byId/{id}", this.invoiceRejectionRequest.getId())).andExpect(status().isOk())
				.andExpect(content().string(Parser.asJsonString(expectedResponse.getBody())));
	}

	@Test
	void findInvoiceRejectionById_shouldFailOnMissmatchingParam() throws Exception {
		// WHEN // THEN
		this.mockMvc.perform(get(Constant.INVOICE_REJECTION_RESOURCE_API + "/byId/{id}", any(String.class)))
				.andExpect(status().isInternalServerError()).andExpect(content().string("Incorrect field type!"));
	}

	@Test
	void updateInvoiceRejection_success() throws Exception {
		// GIVEN
		doNothing().when(invoiceRejectionService).saveInvoiceRejection(Mockito.any(InvoiceRejectionRequest.class), Mockito.anyBoolean());

		// WHEN THEN
		this.mockMvc
				.perform(put(Constant.INVOICE_REJECTION_RESOURCE_API + "/update").contentType(MediaType.APPLICATION_JSON)
						.content(Parser.asJsonString(invoiceRejectionRequest)).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(content().string("Invoice rejection updated successfully!"));
	}

	@Test
	void deleteInvoiceRejectionById_success() throws Exception {
		// GIVEN
		doNothing().when(invoiceRejectionService).deleteInvoiceRejectionById(Mockito.any(Long.class));

		// WHEN // THEN
		this.mockMvc.perform(delete(Constant.INVOICE_REJECTION_RESOURCE_API + "/delete/{id}", Mockito.any(Long.class)))
				.andExpect(status().isOk()).andExpect(content().string("Invoice rejection deleted successfully!"));
	}

	@Test
	void deleteInvoiceRejectionById_shouldFailOnMismatchParameterType() throws Exception {
		// WHEN // THEN
		this.mockMvc.perform(delete(Constant.INVOICE_REJECTION_RESOURCE_API + "/delete/{id}", "toto"))
				.andExpect(status().isInternalServerError()).andExpect(content().string("Incorrect field type!"));
	}
	
}
