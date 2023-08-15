package com.hbtheme.infigestback.controller;

import com.hbtheme.infigestback.dto.InvoiceRequest;
import com.hbtheme.infigestback.dto.InvoiceResponse;
import com.hbtheme.infigestback.dto.InvoiceSimpleResponse;
import com.hbtheme.infigestback.mapper.InvoiceMapper;
import com.hbtheme.infigestback.model.Invoice;
import com.hbtheme.infigestback.model.Patient;
import com.hbtheme.infigestback.model.StateRegisteredNurse;
import com.hbtheme.infigestback.service.InvoiceService;
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
@WebMvcTest(controllers = InvoiceController.class)
@ActiveProfiles("test")
class InvoiceControllerTest {

	private InvoiceRequest invoiceRequest;
	private final List<Invoice> invoices = new ArrayList<>();
	private List<InvoiceSimpleResponse> invoiceSimpleResponses = new ArrayList<>();

	@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private InvoiceService invoiceService;
	
	@MockBean
	private InvoiceMapper invoiceMapper;

	@BeforeEach
	void setUp() {
		this.invoiceRequest = InvoiceRequest.builder().id(2L)
				.stateRegisteredNurseId(1L)
				.patientId(2L)
				.build();
		StateRegisteredNurse stateRegisteredNurse = StateRegisteredNurse.builder()
				.id(invoiceRequest.getStateRegisteredNurseId())
				.firstName("John")
				.lastName("Doe")
				.build();
		Patient patient = Patient.builder()
				.id(invoiceRequest.getPatientId())
				.firstName("Jane")
				.lastName("Doe")
				.build();
		Invoice invoice = Invoice.builder()
				.id(invoiceRequest.getId())
				.stateRegisteredNurse(stateRegisteredNurse)
				.patient(patient)
				.build();
		this.invoices.add(invoice);
		this.invoiceSimpleResponses = this.invoices.stream().map(i->invoiceMapper.toSimpleResponse(i)).toList();
	}
	
	@Test
	void createInvoice_success() throws Exception {
		// GIVEN
		doNothing().when(invoiceService).saveInvoice(Mockito.any(InvoiceRequest.class), Mockito.anyBoolean());

		// WHEN THEN
		this.mockMvc
				.perform(post(Constant.INVOICE_RESOURCE_API).contentType(MediaType.APPLICATION_JSON)
						.content(Parser.asJsonString(invoiceRequest)).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated()).andExpect(content().string("Invoice created successfully!"));

	}
	
	@Test
	void createInvoice_shouldFailOnExceptionDetected() throws Exception {
		// GIVEN
		doThrow(IllegalArgumentException.class).when(invoiceService).saveInvoice(invoiceRequest, false);

		// WHEN THEN
		this.mockMvc
				.perform(post(Constant.INVOICE_RESOURCE_API).contentType(MediaType.APPLICATION_JSON)
						.content(Parser.asJsonString(invoiceRequest)).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(content().string("Error while processing invoice creation"));

	}

	@Test
	void findAllInvoices_success() throws Exception {
		// GIVEN
		ResponseEntity<InvoiceResponse> expectedResponse = new ResponseEntity<>(InvoiceResponse.builder()
				.message(String.format("Successfully returned %s Invoice(s)", invoiceSimpleResponses.size()))
				.invoiceSimpleResponses(invoiceSimpleResponses).build(), HttpStatus.OK);
		when(invoiceService.findAllInvoices()).thenReturn(invoices);

		// WHEN // THEN
		this.mockMvc.perform(get(Constant.INVOICE_RESOURCE_API)).andExpect(status().isOk())
				.andExpect(content().string(Parser.asJsonString(expectedResponse.getBody())));
	}

	@Test
	void findInvoiceById_success() throws Exception {
		// GIVEN
		ResponseEntity<InvoiceResponse> expectedResponse = new ResponseEntity<>(InvoiceResponse.builder()
				.message("Successfully returned 1 Invoice")
				.invoiceSimpleResponses(invoiceSimpleResponses).build(), HttpStatus.OK);
		
	    when(invoiceService.findInvoiceById(Mockito.anyLong())).thenReturn(invoices);

		// WHEN // THEN
		this.mockMvc.perform(get(Constant.INVOICE_RESOURCE_API + "/byId/{id}", this.invoiceRequest.getId())).andExpect(status().isOk())
				.andExpect(content().string(Parser.asJsonString(expectedResponse.getBody())));
	}

	@Test
	void findInvoiceById_shouldFailOnMissmatchingParam() throws Exception {
		// WHEN // THEN
		this.mockMvc.perform(get(Constant.INVOICE_RESOURCE_API + "/byId/{id}", any(String.class)))
				.andExpect(status().isInternalServerError()).andExpect(content().string("Incorrect field type!"));
	}

	@Test
	void updateInvoice_success() throws Exception {
		// GIVEN
		doNothing().when(invoiceService).saveInvoice(Mockito.any(InvoiceRequest.class), Mockito.anyBoolean());

		// WHEN THEN
		this.mockMvc
				.perform(put(Constant.INVOICE_RESOURCE_API + "/update").contentType(MediaType.APPLICATION_JSON)
						.content(Parser.asJsonString(invoiceRequest)).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(content().string("Invoice updated successfully!"));
	}

	@Test
	void deleteInvoiceById_success() throws Exception {
		// GIVEN
		doNothing().when(invoiceService).deleteInvoiceById(Mockito.any(Long.class));

		// WHEN // THEN
		this.mockMvc.perform(delete(Constant.INVOICE_RESOURCE_API + "/delete/{id}", Mockito.any(Long.class)))
				.andExpect(status().isOk()).andExpect(content().string("Invoice deleted successfully!"));
	}

	@Test
	void deleteInvoiceById_shouldFailOnMismatchParameterType() throws Exception {
		// WHEN // THEN
		this.mockMvc.perform(delete(Constant.INVOICE_RESOURCE_API + "/delete/{id}", "toto"))
				.andExpect(status().isInternalServerError()).andExpect(content().string("Incorrect field type!"));
	}
	
}
