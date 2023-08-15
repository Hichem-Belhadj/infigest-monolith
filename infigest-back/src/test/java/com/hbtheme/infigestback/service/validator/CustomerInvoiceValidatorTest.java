package com.hbtheme.infigestback.service.validator;

import com.hbtheme.infigestback.dto.CustomerInvoiceRequest;
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
class CustomerInvoiceValidatorTest {

	@InjectMocks
	private CustomerInvoiceValidator customerInvoiceValidator;
	
	@ParameterizedTest
	@MethodSource("provideValidValuesForCustomerInvoice")
	void validate_success(Long id, boolean isIdRequired, Long stateRegisteredNurseId, List<String> errors) {
		// GIVEN
		CustomerInvoiceRequest customerInvoiceRequest = CustomerInvoiceRequest.builder()
				.id(id)
				.stateRegisteredNurseId(stateRegisteredNurseId)
				.build();
		
		// WHEN
		List<String> expectedErrors = customerInvoiceValidator.validate(customerInvoiceRequest, isIdRequired);
		
		// THEN
		assertThat(expectedErrors).hasSameSizeAs(errors);
		assertThat(errors).containsAll(expectedErrors);
	}

	private static List<Arguments> provideValidValuesForCustomerInvoice() {
		List<Arguments> args = new ArrayList<>();
		List<String> errors1 = Arrays.asList(
				"The id must be submitted",
				"The nurse's id must be submitted"
		);
		args.add(Arguments.of(null, true, null, errors1));

		return args;
	}
	
}
