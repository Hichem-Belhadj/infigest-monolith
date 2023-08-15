package com.hbtheme.infigestback.service.validator;

import com.hbtheme.infigestback.dto.InvoiceRejectionRequest;
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
class InvoiceRejectionValidatorTest {

	@InjectMocks
	private InvoiceRejectionValidator invoiceRejectionValidator;

	@ParameterizedTest
	@MethodSource("provideValidValuesForInvoiceRejection")
	void validate_success(Long id, boolean isIdRequired, Long stateRegisteredNurseId, Long patientId,
			String feedBackDate, String paymentDate, List<String> errors) {

		// GIVEN
		InvoiceRejectionRequest invoiceRejectionRequest = InvoiceRejectionRequest.builder().id(id)
				.stateRegisteredNurseId(stateRegisteredNurseId).patientId(patientId).feedBackDate(feedBackDate)
				.paymentDate(paymentDate).build();

		// WHEN
		List<String> expectedErrors = invoiceRejectionValidator.validate(invoiceRejectionRequest, isIdRequired);

		// THEN
		assertThat(expectedErrors).hasSameSizeAs(errors);
		assertThat(errors).containsAll(expectedErrors);
	}

	private static List<Arguments> provideValidValuesForInvoiceRejection() {
		List<Arguments> args = new ArrayList<>();
		List<String> errors1 = Arrays.asList("The id must be submitted", "The nurse's id must be submitted",
				"The patient's id must be submitted", "The date format is invalid for feedback",
				"The date format is invalid for payment");
		args.add(Arguments.of(null, true, null, null, "aaa", "aaa", errors1));

		List<String> errors2 = List.of("The date format is invalid for feedback");
		args.add(Arguments.of(null, false, 1L, 2L, "21/11/2022", "2023-05-13", errors2));

		return args;
	}

}
