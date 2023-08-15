package com.hbtheme.infigestback.service;

import com.hbtheme.infigestback.dto.InvoiceRejectionRequest;
import com.hbtheme.infigestback.mapper.InvoiceRejectionMapper;
import com.hbtheme.infigestback.model.InvoiceRejection;
import com.hbtheme.infigestback.model.Patient;
import com.hbtheme.infigestback.model.StateRegisteredNurse;
import com.hbtheme.infigestback.repository.InvoiceRejectionDao;
import com.hbtheme.infigestback.service.validator.InvoiceRejectionValidator;
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
class InvoiceRejectionServiceTest {
	
	private InvoiceRejectionRequest invoiceRejectionRequest;
	private InvoiceRejection invoiceRejection;
	private final List<InvoiceRejection> invoiceRejections = new ArrayList<>();
	private final List<StateRegisteredNurse> stateRegisteredNurses = new ArrayList<>();
	private final List<Patient> patients = new ArrayList<>();
	private final List<String> errors = new ArrayList<>();
	private final Long invoiceRejectionId = 42L;
	
	@Mock
	private InvoiceRejectionDao invoiceRejectionDao;
	
	@Mock
	private InvoiceRejectionValidator invoiceRejectionValidator;
	
	@Mock
	private StateRegisteredNurseService stateRegisteredNurseService;
	
	@Mock
	private PatientService patientService;
	
	@Mock
	private InvoiceRejectionMapper invoiceRejectionMapper;
	
	@InjectMocks
	@Spy
	private InvoiceRejectionService invoiceRejectionService;
	
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
		this.invoiceRejectionRequest = InvoiceRejectionRequest.builder()
				.stateRegisteredNurseId(1L)
				.patientId(2L)
				.build();
		this.invoiceRejection = InvoiceRejection.builder()
				.id(42L)
				.stateRegisteredNurse(stateRegisteredNurses.get(0))
				.patient(patients.get(0))
				.build();
		this.invoiceRejections.add(invoiceRejection);
	}

	@Test
	void saveInvoiceRejection_success() {
		// GIVEN
		when(invoiceRejectionValidator.validate(invoiceRejectionRequest, false)).thenReturn(errors);
		when(stateRegisteredNurseService.findStateRegisteredNurseById(invoiceRejectionRequest.getStateRegisteredNurseId())).thenReturn(stateRegisteredNurses);
		when(patientService.findPatientById(invoiceRejectionRequest.getPatientId())).thenReturn(patients);
		when(invoiceRejectionMapper.toModel(invoiceRejectionRequest, stateRegisteredNurses.get(0), patients.get(0))).thenReturn(invoiceRejection);
		when(invoiceRejectionDao.save(invoiceRejection)).thenAnswer(i -> i.getArguments()[0]);
		
		// WHEN
		invoiceRejectionService.saveInvoiceRejection(invoiceRejectionRequest, false);
		
		//THEN
		verify(invoiceRejectionValidator, times(1)).validate(invoiceRejectionRequest, false);
	}
	
	@Test
	void saveInvoiceRejection_shouldFailOnNullRequest() {
		this.invoiceRejectionRequest = null;	
		
		// WHEN THEN
		assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> invoiceRejectionService.saveInvoiceRejection(invoiceRejectionRequest, false));

	}
	
	@Test
	void findInvoiceRejectionById_success() {
		// GIVEN
		Mockito.when(invoiceRejectionDao.findById(invoiceRejectionId)).thenReturn(Optional.of(invoiceRejection));
		
		// WHEN
		List<InvoiceRejection> expectedInvoiceRejections = invoiceRejectionService.findInvoiceRejectionById(invoiceRejectionId);
		
		// THEN
		assertThat(expectedInvoiceRejections).isEqualTo(invoiceRejections);
	}
	
	@Test
	void findInvoiceRejectionById_shouldFailOnBadId() {
		// GIVEN
		Mockito.when(invoiceRejectionDao.findById(invoiceRejectionId)).thenReturn(Optional.empty());
		
		// WHEN THEN
		assertThatExceptionOfType(NoSuchElementException.class).isThrownBy(() -> invoiceRejectionService.findInvoiceRejectionById(invoiceRejectionId));
	}
	
	@Test
	void findInvoiceRejectionById_shouldFailOnNullId() {
		// WHEN THEN
		assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> invoiceRejectionService.findInvoiceRejectionById(null));
	}
	
	@Test
	void deleteInvoiceRejectionById_success() {
		// GIVEN
		doNothing().when(invoiceRejectionDao).deleteById(invoiceRejectionId);
		Mockito.doReturn(invoiceRejections).when(invoiceRejectionService).findInvoiceRejectionById(invoiceRejectionId);
		
		// WHEN
		invoiceRejectionService.deleteInvoiceRejectionById(invoiceRejectionId);
		
		// THEN
		verify(invoiceRejectionDao, times(1)).deleteById(invoiceRejectionId);
	}
	
	@Test
	void deleteInvoiceRejectionById_shouldFailOnBadId() {
		// GIVEN
		List<InvoiceRejection> emptyInvoiceRejections = new ArrayList<>();
		Mockito.doReturn(emptyInvoiceRejections).when(invoiceRejectionService).findInvoiceRejectionById(invoiceRejectionId);
		
		// WHEN THEN
		assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> invoiceRejectionService.deleteInvoiceRejectionById(invoiceRejectionId));
	}
	
	@Test
	void deleteInvoiceRejectionById_shouldFailOnNullId() {
		// WHEN THEN
		assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> invoiceRejectionService.deleteInvoiceRejectionById(null));
	}
	
}
