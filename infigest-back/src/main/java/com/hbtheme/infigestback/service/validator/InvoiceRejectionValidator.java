package com.hbtheme.infigestback.service.validator;

import com.hbtheme.infigestback.dto.InvoiceRejectionRequest;
import com.hbtheme.infigestback.service.PatientService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class InvoiceRejectionValidator extends BaseValidator {

	public InvoiceRejectionValidator(PatientService patientService) {
		super(patientService);
	}

	public List<String> validate(InvoiceRejectionRequest invoiceRejectionRequest, boolean isIdRequired) {
		List<String> errors = new ArrayList<>();
		errors.add(validateId(invoiceRejectionRequest.getId(), isIdRequired));
		errors.add(validateStateRegisteredNurseId(invoiceRejectionRequest.getStateRegisteredNurseId()));
		errors.add(validateStateRegisteredNurse(invoiceRejectionRequest.getPatientId(), invoiceRejectionRequest.getStateRegisteredNurseId()));
		errors.add(validatePatientId(invoiceRejectionRequest.getPatientId()));
		errors.add(validateDateFormat(invoiceRejectionRequest.getFeedBackDate(), "feedback"));
		errors.add(validateDateFormat(invoiceRejectionRequest.getPaymentDate(), "payment"));
		errors.removeIf(String::isEmpty);
		return errors;
	}

}
