package com.hbtheme.infigestback.service.validator;

import com.hbtheme.infigestback.dto.StateRegisteredNurseRequest;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class StateRegisteredNurseServiceValidatorTest {

	@InjectMocks
	private StateRegisteredNurseValidator stateRegisteredNurseValidator;

	@ParameterizedTest
	@MethodSource("provideValidValuesForNurse")
	void validate_success(Long id, boolean isIdRequired, String firstName, String lastName,
			List<String> errors) {
		// GIVEN
		StateRegisteredNurseRequest stateRegisteredNurseRequest = StateRegisteredNurseRequest.builder()
				.id(id)
				.firstName(firstName)
				.lastName(lastName)
				.build();

		// WHEN
		List<String> expectedErrors = stateRegisteredNurseValidator.validate(stateRegisteredNurseRequest, isIdRequired);

		// THEN
		assertThat(expectedErrors).hasSameSizeAs(errors);
		assertThat(errors).containsAll(expectedErrors);
	}

	private static List<Arguments> provideValidValuesForNurse() {
		List<Arguments> args = new ArrayList<>();
		List<String> errors1 = Arrays.asList("The id must be submitted",
				"The first name must be submitted", "The last name must be submitted");
		args.add(Arguments.of(null, true, null, null, null, errors1));

		return args;
	}
	
}
