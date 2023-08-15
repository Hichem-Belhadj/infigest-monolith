package com.hbtheme.infigestback.service.validator;

import com.hbtheme.infigestback.dto.InvoiceRequest;
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
class InvoiceValidatorTest {

	@InjectMocks
	private InvoiceValidator invoiceValidator;

	@ParameterizedTest
	@MethodSource("provideValidValuesForInvoice")
	void validate_success(Long id, boolean isIdRequired, Long stateRegisteredNurseId, Long patientId,
			String invoiceDate, String careStartDate, String careEndDate, List<String> errors) {

		// GIVEN
		InvoiceRequest invoiceRequest = InvoiceRequest.builder()
				.id(id)
				.stateRegisteredNurseId(stateRegisteredNurseId)
				.patientId(patientId)
				.invoiceDate(invoiceDate)
				.careStartDate(careStartDate)
				.careEndDate(careEndDate)
				.build();
		
		// WHEN
		List<String> expectedErrors = invoiceValidator.validate(invoiceRequest, isIdRequired);
		
		// THEN
		assertThat(expectedErrors).hasSameSizeAs(errors);
		assertThat(errors).containsAll(expectedErrors);
	}

	private static List<Arguments> provideValidValuesForInvoice() {
		List<Arguments> args = new ArrayList<>();
		List<String> errors1 = Arrays.asList(
				"The id must be submitted",
				"The nurse's id must be submitted",
				"The patient's id must be submitted",
				"The date format is invalid for invoice",
				"The date format is invalid for care start date",
				"The date format is invalid for care end date"
		);
		args.add(Arguments.of(null, true, null, null, "aaa", "aaa", "aaa", errors1));

		List<String> errors2 = Arrays.asList(
				"The date format is invalid for invoice",
				"The start date of care must precede the end date"
		);
		args.add(Arguments.of(null, false, 1L, 2L, "21/11/2022", "2023-05-13", "2023-05-10", errors2));

		return args;
	}

}
