package com.hbtheme.infigestback.service;

import com.hbtheme.infigestback.dto.InsuranceRequest;
import com.hbtheme.infigestback.mapper.InsuranceMapper;
import com.hbtheme.infigestback.model.Insurance;
import com.hbtheme.infigestback.repository.InsuranceDao;
import com.hbtheme.infigestback.service.validator.InsuranceValidator;
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
class InsuranceServiceTest {
	
	private InsuranceRequest insuranceRequest;
	private Insurance insurance;
	private final List<Insurance> insurances = new ArrayList<>();
	private final List<String> errors = new ArrayList<>();
	private final Long insuranceId = 42L;
	
	@Mock
	private InsuranceDao insuranceDao;
	
	@Mock
	private InsuranceValidator insuranceValidator;
	
	@Mock
	private InsuranceMapper insuranceMapper;
	
	@InjectMocks
	@Spy
	private InsuranceService insuranceService;
	
	@BeforeEach
	void setUp() {
		this.insuranceRequest = InsuranceRequest.builder()
				.name("Macif")
				.build();
		this.insurance = Insurance.builder()
				.id(insuranceRequest.getId())
				.name(insuranceRequest.getName())
				.build();
		this.insurances.add(insurance);
	}
	
	@Test
	void saveInsurance_success() {
		// GIVEN
		when(insuranceValidator.validate(insuranceRequest, false)).thenReturn(errors);
		when(insuranceDao.save(insurance)).thenAnswer(i -> i.getArguments()[0]);
		when(insuranceMapper.toModel(insuranceRequest)).thenReturn(insurance);
		
		// WHEN
		insuranceService.saveInsurance(insuranceRequest, false);
		
		//THEN
		verify(insuranceValidator, times(1)).validate(insuranceRequest, false);
	}
	
	@Test
	void saveInvoice_shouldFailOnNullRequest() {
		this.insuranceRequest = null;	
		
		// WHEN THEN
		assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> insuranceService.saveInsurance(insuranceRequest, false));

	}
	
	@Test
	void findInsuranceById_success() {
		// GIVEN
		Mockito.when(insuranceDao.findById(insuranceId)).thenReturn(Optional.of(insurance));
		
		// WHEN
		List<Insurance> expectedInsurances = insuranceService.findInsuranceById(insuranceId);
		
		// THEN
		assertThat(expectedInsurances).isEqualTo(insurances);
	}
	
	@Test
	void findInsuranceById_shouldFailOnBadId() {
		// GIVEN
		Mockito.when(insuranceDao.findById(insuranceId)).thenReturn(Optional.empty());
		
		// WHEN THEN
		assertThatExceptionOfType(NoSuchElementException.class).isThrownBy(() -> insuranceService.findInsuranceById(insuranceId));
	}
	
	@Test
	void findInsuranceById_shouldFailOnNullId() {
		// WHEN THEN
		assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> insuranceService.findInsuranceById(null));
	}
	
	@Test
	void deleteInsuranceById_success() {
		// GIVEN
		doNothing().when(insuranceDao).deleteById(insuranceId);
		Mockito.doReturn(insurances).when(insuranceService).findInsuranceById(insuranceId);
		
		// WHEN
		insuranceService.deleteInsuranceById(insuranceId);
		
		// THEN
		verify(insuranceDao, times(1)).deleteById(insuranceId);
	}
	
	@Test
	void deleteInsuranceById_shouldFailOnBadId() {
		// GIVEN
		List<Insurance> emptyInsurances = new ArrayList<>();
		Mockito.doReturn(emptyInsurances).when(insuranceService).findInsuranceById(insuranceId);
		
		// WHEN THEN
		assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> insuranceService.deleteInsuranceById(insuranceId));
	}
	
	@Test
	void deleteInsuranceById_shouldFailOnNullId() {
		// WHEN THEN
		assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> insuranceService.deleteInsuranceById(null));
	}

}
