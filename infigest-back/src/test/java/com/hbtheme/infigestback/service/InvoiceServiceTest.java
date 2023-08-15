package com.hbtheme.infigestback.service;

import com.hbtheme.infigestback.dto.InvoiceRequest;
import com.hbtheme.infigestback.mapper.InvoiceMapper;
import com.hbtheme.infigestback.model.Invoice;
import com.hbtheme.infigestback.model.Patient;
import com.hbtheme.infigestback.model.StateRegisteredNurse;
import com.hbtheme.infigestback.repository.InvoiceDao;
import com.hbtheme.infigestback.service.validator.InvoiceValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class InvoiceServiceTest {
	
	private InvoiceRequest invoiceRequest;
	private Invoice invoice;
	private final List<Invoice> invoices = new ArrayList<>();
	private final List<StateRegisteredNurse> stateRegisteredNurses = new ArrayList<>();
	private final List<Patient> patients = new ArrayList<>();
	private final List<String> errors = new ArrayList<>();
	private final Long invoiceId = 42L;
	
	@Mock
	private InvoiceDao invoiceDao;
	
	@Mock
	private InvoiceValidator invoiceValidator;
	
	@Mock
	private StateRegisteredNurseService stateRegisteredNurseService;
	
	@Mock
	private PatientService patientService;
	
	@Mock
	private InvoiceMapper invoiceMapper;
	
	@InjectMocks
	@Spy
	private InvoiceService invoiceService;
	
	@BeforeEach
	void setUp() {
		this.patients.add(Patient.builder()
				.id(2L)
				.firstName("Jane")
				.lastName("Doe")
				.build());
		this.stateRegisteredNurses.add(StateRegisteredNurse.builder()
				.id(1L)
				.firstName("John")
				.lastName("Doe")
				.build());
		this.invoiceRequest = InvoiceRequest.builder()
				.stateRegisteredNurseId(1L)
				.patientId(2L)
				.build();
		this.invoice = Invoice.builder()
				.id(42L)
				.stateRegisteredNurse(stateRegisteredNurses.get(0))
				.patient(patients.get(0))
				.build();
		this.invoices.add(invoice);
	}

	@Test
	void saveInvoice_success() {
		// GIVEN
		when(invoiceValidator.validate(invoiceRequest, false)).thenReturn(errors);
		when(stateRegisteredNurseService.findStateRegisteredNurseById(invoiceRequest.getStateRegisteredNurseId())).thenReturn(stateRegisteredNurses);
		when(patientService.findPatientById(invoiceRequest.getPatientId())).thenReturn(patients);
		when(invoiceMapper.toModel(invoiceRequest, stateRegisteredNurses.get(0), patients.get(0))).thenReturn(invoice);
		when(invoiceDao.save(invoice)).thenAnswer(i -> i.getArguments()[0]);
		
		// WHEN
		invoiceService.saveInvoice(invoiceRequest, false);
		
		//THEN
		verify(invoiceValidator, times(1)).validate(invoiceRequest, false);
	}
	
	@Test
	void saveInvoice_shouldFailOnNullRequest() {
		this.invoiceRequest = null;	
		
		// WHEN THEN
		assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> invoiceService.saveInvoice(invoiceRequest, false));

	}
	
	@Test
	void findInvoiceById_success() {
		// GIVEN
		Mockito.when(invoiceDao.findById(invoiceId)).thenReturn(Optional.of(invoice));
		
		// WHEN
		List<Invoice> expectedInvoices = invoiceService.findInvoiceById(invoiceId);
		
		// THEN
		assertThat(expectedInvoices).isEqualTo(invoices);
	}
	
	@Test
	void findInvoiceById_shouldFailOnBadId() {
		// GIVEN
		Mockito.when(invoiceDao.findById(invoiceId)).thenReturn(Optional.empty());
		
		// WHEN THEN
		assertThatExceptionOfType(NoSuchElementException.class).isThrownBy(() -> invoiceService.findInvoiceById(invoiceId));
	}
	
	@Test
	void findInvoiceById_shouldFailOnNullId() {
		// WHEN THEN
		assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> invoiceService.findInvoiceById(null));
	}
	
	@Test
	void deleteInvoiceById_success() {
		// GIVEN
		doNothing().when(invoiceDao).deleteById(invoiceId);
		Mockito.doReturn(invoices).when(invoiceService).findInvoiceById(invoiceId);
		
		// WHEN
		invoiceService.deleteInvoiceById(invoiceId);
		
		// THEN
		verify(invoiceDao, times(1)).deleteById(invoiceId);
	}
	
	@Test
	void deleteInvoiceById_shouldFailOnBadId() {
		// GIVEN
		List<Invoice> emptyInvoices = new ArrayList<>();
		Mockito.doReturn(emptyInvoices).when(invoiceService).findInvoiceById(invoiceId);
		
		// WHEN THEN
		assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> invoiceService.deleteInvoiceById(invoiceId));
	}
	
	@Test
	void deleteInvoiceById_shouldFailOnNullId() {
		// WHEN THEN
		assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> invoiceService.deleteInvoiceById(null));
	}

}
