package com.hbtheme.infigestback.service;

import com.hbtheme.infigestback.dto.PatientRequest;
import com.hbtheme.infigestback.mapper.PatientMapper;
import com.hbtheme.infigestback.model.Patient;
import com.hbtheme.infigestback.model.StateRegisteredNurse;
import com.hbtheme.infigestback.repository.PatientDao;
import com.hbtheme.infigestback.service.validator.PatientValidator;
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
class PatientServiceTest {
	
	private PatientRequest patientRequest;
	private final List<String> errors = new ArrayList<>();
	private final List<Patient> patients = new ArrayList<>();
	private final List<StateRegisteredNurse> stateRegisteredNurses = new ArrayList<>();
	private Patient patient;
	private final Long patientId = 42L;
	
	@Mock
	private PatientDao patientDao;
	
	@InjectMocks
	@Spy
	private PatientService patientService;
	
	@Mock
	private PatientValidator patientValidator;
	
	@Mock
	private StateRegisteredNurseService stateRegisteredNurseService;
	
	@Mock
	private PatientMapper patientMapper;
	
	@BeforeEach
	void setUp() {
		this.stateRegisteredNurses.add(StateRegisteredNurse.builder()
				.id(1L)
				.firstName("John")
				.lastName("Doe")
				.build());
		this.patientRequest = PatientRequest.builder()
				.firstName("John")
				.lastName("Doe")
				.stateRegisteredNursesIds(List.of(1L))
				.build();
		this.patient = Patient.builder()
				.id(42L)
				.firstName("Jane")
				.lastName("Doe")
				.stateRegisteredNurses(List.of(stateRegisteredNurses.get(0)))
				.build();
		this.patients.add(patient);
	}
	
	@Test
	void savePatient_success() {
		// GIVEN
		when(patientValidator.validate(patientRequest, false)).thenReturn(errors);
		when(stateRegisteredNurseService.findStateRegisteredNurseById(patientRequest.getStateRegisteredNursesIds().get(0))).thenReturn(stateRegisteredNurses);
		when(patientMapper.toModel(patientRequest, stateRegisteredNurses)).thenReturn(patient);
		when(patientDao.save(patient)).thenAnswer(i -> i.getArguments()[0]);
		
		// WHEN
		patientService.savePatient(patientRequest, false);
		
		//THEN
		verify(patientValidator, times(1)).validate(patientRequest, false);
	}
	
	@Test
	void savePatient_shouldFailOnNullRequest() {
		this.patientRequest = null;	
		
		// WHEN THEN
		assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> patientService.savePatient(patientRequest, false));

	}
	
	@Test
	void findPatientById_success() {
		// GIVEN
		Mockito.when(patientDao.findById(patientId)).thenReturn(Optional.of(patient));
		
		// WHEN
		List<Patient> expectedPatients = patientService.findPatientById(patientId);
		
		// THEN
		assertThat(expectedPatients).isEqualTo(patients);
	}
	
	@Test
	void findPatientById_shouldFailOnBadId() {
		// GIVEN
		Mockito.when(patientDao.findById(patientId)).thenReturn(Optional.empty());
		
		// WHEN THEN
		assertThatExceptionOfType(NoSuchElementException.class).isThrownBy(() -> patientService.findPatientById(patientId));
	}
	
	@Test
	void findPatientById_shouldFailOnNullId() {
		// WHEN THEN
		assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> patientService.findPatientById(null));
	}
	
	@Test
	void deletePatientById_success() {
		// GIVEN
		doNothing().when(patientDao).deleteById(patientId);
		Mockito.doReturn(patients).when(patientService).findPatientById(patientId);
		
		// WHEN
		patientService.deletePatientById(patientId);
		
		// THEN
		verify(patientDao, times(1)).deleteById(patientId);
	}
	
	@Test
	void deletePatientById_shouldFailOnBadId() {
		// GIVEN
		List<Patient> emptyPatients= new ArrayList<>();
		Mockito.doReturn(emptyPatients).when(patientService).findPatientById(patientId);
		
		// WHEN THEN
		assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> patientService.deletePatientById(patientId));
	}
	
	@Test
	void deletePatientById_shouldFailOnNullId() {
		// WHEN THEN
		assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> patientService.deletePatientById(null));
	}

}
