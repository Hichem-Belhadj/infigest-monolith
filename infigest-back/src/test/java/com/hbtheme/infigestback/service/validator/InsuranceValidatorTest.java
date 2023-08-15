package com.hbtheme.infigestback.service.validator;

import com.hbtheme.infigestback.dto.InsuranceRequest;
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
class InsuranceValidatorTest {

	@InjectMocks
	private InsuranceValidator insuranceValidator;

	@ParameterizedTest
	@MethodSource("provideValidValuesForInsurance")
	void validate_success(Long id, boolean isIdRequired, String name, List<String> errors) {

		// GIVEN
		InsuranceRequest insuranceRequest = InsuranceRequest.builder()
				.id(id)
				.name(name)
				.build();
		
		// WHEN
		List<String> expectedErrors = insuranceValidator.validate(insuranceRequest, isIdRequired);
		
		// THEN
		assertThat(expectedErrors).hasSameSizeAs(errors);
		assertThat(errors).containsAll(expectedErrors);
	}

	private static List<Arguments> provideValidValuesForInsurance() {
		List<Arguments> args = new ArrayList<>();
		List<String> errors1 = Arrays.asList(
				"The id must be submitted",
				"The insurance name must be submitted"
		);
		args.add(Arguments.of(null, true, null, errors1));

		return args;
	}
	
}
