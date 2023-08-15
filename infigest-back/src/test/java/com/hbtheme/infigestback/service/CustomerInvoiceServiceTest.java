package com.hbtheme.infigestback.service;

import com.hbtheme.infigestback.dto.CustomerInvoiceRequest;
import com.hbtheme.infigestback.mapper.CustomerInvoiceMapper;
import com.hbtheme.infigestback.model.CustomerInvoice;
import com.hbtheme.infigestback.model.Invoice;
import com.hbtheme.infigestback.model.StateRegisteredNurse;
import com.hbtheme.infigestback.repository.CustomerInvoiceDao;
import com.hbtheme.infigestback.service.validator.CustomerInvoiceValidator;
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
class CustomerInvoiceServiceTest {
	
	private CustomerInvoiceRequest customerInvoiceRequest;
	private CustomerInvoice customerInvoice;
	private final List<CustomerInvoice> customerInvoices = new ArrayList<>();
	private final List<String> errors = new ArrayList<>();
	private final List<StateRegisteredNurse> stateRegisteredNurses = new ArrayList<>();
	private final Long customerInvoiceId = 42L;
	
	@Mock
	private CustomerInvoiceDao customerInvoiceDao;
	
	@Mock
	private CustomerInvoiceValidator customerInvoiceValidator;
	
	@Mock
	private StateRegisteredNurseService stateRegisteredNurseService;
	
	@Mock
	private CustomerInvoiceMapper customerInvoiceMapper;
	
	@InjectMocks
	@Spy
	private CustomerInvoiceService customerInvoiceService;
	
	@BeforeEach
	void setUp() {
		this.stateRegisteredNurses.add(StateRegisteredNurse.builder()
				.id(1L)
				.firstName("John")
				.lastName("Doe")
				.build());
		this.customerInvoiceRequest = CustomerInvoiceRequest.builder()
				.stateRegisteredNurseId(1L)
				.build();
		this.customerInvoice = CustomerInvoice.builder()
				.id(42L)
				.stateRegisteredNurse(stateRegisteredNurses.get(0))
				.build();
		this.customerInvoices.add(customerInvoice);
	}

	@Test
	void saveCustomerInvoice_success() {
		// GIVEN
		when(customerInvoiceValidator.validate(customerInvoiceRequest, false)).thenReturn(errors);
		when(stateRegisteredNurseService.findStateRegisteredNurseById(customerInvoiceRequest.getStateRegisteredNurseId())).thenReturn(stateRegisteredNurses);
		when(customerInvoiceMapper.toModel(customerInvoiceRequest, stateRegisteredNurses.get(0))).thenReturn(customerInvoice);
		when(customerInvoiceDao.save(customerInvoice)).thenAnswer(i -> i.getArguments()[0]);
		
		// WHEN
		customerInvoiceService.saveCustomerInvoice(customerInvoiceRequest, false);
		
		//THEN
		verify(customerInvoiceValidator, times(1)).validate(customerInvoiceRequest, false);
	}
	
	@Test
	void saveCustomerInvoice_shouldFailOnNullRequest() {
		this.customerInvoiceRequest = null;	
		
		// WHEN THEN
		assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> customerInvoiceService.saveCustomerInvoice(customerInvoiceRequest, false));

	}
	
	@Test
	void findCustomerInvoiceById_success() {
		// GIVEN
		Mockito.when(customerInvoiceDao.findById(customerInvoiceId)).thenReturn(Optional.of(customerInvoice));
		
		// WHEN
		List<CustomerInvoice> expectedInvoices = customerInvoiceService.findCustomerInvoiceById(customerInvoiceId);
		
		// THEN
		assertThat(expectedInvoices).isEqualTo(customerInvoices);
	}
	
	@Test
	void findCustomerInvoiceById_shouldFailOnBadId() {
		// GIVEN
		Mockito.when(customerInvoiceDao.findById(customerInvoiceId)).thenReturn(Optional.empty());
		
		// WHEN THEN
		assertThatExceptionOfType(NoSuchElementException.class).isThrownBy(() -> customerInvoiceService.findCustomerInvoiceById(customerInvoiceId));
	}
	
	@Test
	void findCustomerInvoiceById_shouldFailOnNullId() {
		// WHEN THEN
		assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> customerInvoiceService.findCustomerInvoiceById(null));
	}
	
	@Test
	void deleteCustomerInvoiceById_success() {
		// GIVEN
		doNothing().when(customerInvoiceDao).deleteById(customerInvoiceId);
		Mockito.doReturn(customerInvoices).when(customerInvoiceService).findCustomerInvoiceById(customerInvoiceId);
		
		// WHEN
		customerInvoiceService.deleteCustomerInvoiceById(customerInvoiceId);
		
		// THEN
		verify(customerInvoiceDao, times(1)).deleteById(customerInvoiceId);
	}
	
	@Test
	void deleteCustomerInvoiceById_shouldFailOnBadId() {
		// GIVEN
		List<Invoice> emptyInvoices = new ArrayList<>();
		Mockito.doReturn(emptyInvoices).when(customerInvoiceService).findCustomerInvoiceById(customerInvoiceId);
		
		// WHEN THEN
		assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> customerInvoiceService.deleteCustomerInvoiceById(customerInvoiceId));
	}
	
	@Test
	void deleteCustomerInvoiceById_shouldFailOnNullId() {
		// WHEN THEN
		assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> customerInvoiceService.deleteCustomerInvoiceById(null));
	}
	
}
