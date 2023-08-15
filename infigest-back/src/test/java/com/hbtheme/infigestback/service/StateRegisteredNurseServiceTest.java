package com.hbtheme.infigestback.service;

import com.hbtheme.infigestback.dto.StateRegisteredNurseRequest;
import com.hbtheme.infigestback.mapper.StateRegisteredNurseMapper;
import com.hbtheme.infigestback.model.StateRegisteredNurse;
import com.hbtheme.infigestback.repository.StateRegisteredNurseDao;
import com.hbtheme.infigestback.service.validator.StateRegisteredNurseValidator;
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
class StateRegisteredNurseServiceTest {
	
	private StateRegisteredNurseRequest stateRegisteredNurseRequest;
	private final List<StateRegisteredNurse> stateRegisteredNurses = new ArrayList<>();
	private final List<String> errors = new ArrayList<>();
	private StateRegisteredNurse stateRegisteredNurse;
	private final Long stateRegisteredNurseId = 42L;
	
	@Mock
	private StateRegisteredNurseValidator stateRegisteredNurseValidator;
	
	@Mock
	private StateRegisteredNurseMapper stateRegisteredNurseMapper;
	
	@Mock
	private StateRegisteredNurseDao stateRegisteredNurseDao;
	
	@InjectMocks
	@Spy
	private StateRegisteredNurseService stateRegisteredNurseService;
	
	@BeforeEach
	void setUp() {
		this.stateRegisteredNurseRequest = StateRegisteredNurseRequest.builder()
				.id(42L)
				.firstName("John")
				.lastName("Doe")
				.build();
		this.stateRegisteredNurse = StateRegisteredNurse.builder()
				.id(1L)
				.firstName("Jane")
				.lastName("Doe")
				.build();
		this.stateRegisteredNurses.add(stateRegisteredNurse);
	}
	
	@Test
	void saveStateRegisteredNurse_success() {
		// GIVEN
		when(stateRegisteredNurseValidator.validate(stateRegisteredNurseRequest, false)).thenReturn(errors);
		when(stateRegisteredNurseMapper.toModel(stateRegisteredNurseRequest)).thenReturn(stateRegisteredNurse);
		when(stateRegisteredNurseDao.save(stateRegisteredNurse)).thenAnswer(i -> i.getArguments()[0]);
		
		// WHEN
		stateRegisteredNurseService.saveStateRegisteredNurse(stateRegisteredNurseRequest, false);
		
		//THEN
		verify(stateRegisteredNurseValidator, times(1)).validate(stateRegisteredNurseRequest, false);
	}
	
	@Test
	void saveStateRegisteredNurse_shouldFailOnNullRequest() {
		this.stateRegisteredNurseRequest = null;	
		
		// WHEN THEN
		assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> stateRegisteredNurseService.saveStateRegisteredNurse(stateRegisteredNurseRequest, false));

	}
	
	@Test
	void findStateRegisteredNurseById_success() {
		// GIVEN
		Mockito.when(stateRegisteredNurseDao.findById(stateRegisteredNurseId)).thenReturn(Optional.of(stateRegisteredNurse));
		
		// WHEN
		List<StateRegisteredNurse> expectedStateRegisteredNurses= stateRegisteredNurseService.findStateRegisteredNurseById(stateRegisteredNurseId);
		
		// THEN
		assertThat(expectedStateRegisteredNurses).isEqualTo(stateRegisteredNurses);
	}
	
	@Test
	void findStateRegisteredNurseById_shouldFailOnBadId() {
		// GIVEN
		Mockito.when(stateRegisteredNurseDao.findById(stateRegisteredNurseId)).thenReturn(Optional.empty());
		
		// WHEN THEN
		assertThatExceptionOfType(NoSuchElementException.class).isThrownBy(() -> stateRegisteredNurseService.findStateRegisteredNurseById(stateRegisteredNurseId));
	}
	
	@Test
	void findStateRegisteredNurseById_shouldFailOnNullId() {
		// WHEN THEN
		assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> stateRegisteredNurseService.findStateRegisteredNurseById(null));
	}
	
	@Test
	void deleteStateRegisteredNurseById_success() {
		// GIVEN
		doNothing().when(stateRegisteredNurseDao).deleteById(stateRegisteredNurseId);
		Mockito.doReturn(stateRegisteredNurses).when(stateRegisteredNurseService).findStateRegisteredNurseById(stateRegisteredNurseId);
		
		// WHEN
		stateRegisteredNurseService.deleteStateRegisteredNurseById(stateRegisteredNurseId);
		
		// THEN
		verify(stateRegisteredNurseDao, times(1)).deleteById(stateRegisteredNurseId);
	}
	
	@Test
	void deleteStateRegisteredNurseById_shouldFailOnBadId() {
		// GIVEN
		List<StateRegisteredNurse> emptyStateRegisteredNurses= new ArrayList<>();
		Mockito.doReturn(emptyStateRegisteredNurses).when(stateRegisteredNurseService).findStateRegisteredNurseById(stateRegisteredNurseId);
		
		// WHEN THEN
		assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> stateRegisteredNurseService.deleteStateRegisteredNurseById(stateRegisteredNurseId));
	}
	
	@Test
	void deleteStateRegisteredNurseById_shouldFailOnNullId() {
		// WHEN THEN
		assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> stateRegisteredNurseService.deleteStateRegisteredNurseById(null));
	}
	
}
