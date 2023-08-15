package com.hbtheme.infigestback.service.validator;

import com.hbtheme.infigestback.dto.PatientRequest;
import com.hbtheme.infigestback.service.CustomerInvoiceService;
import com.hbtheme.infigestback.service.PatientService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PatientValidator extends BaseValidator {

	public PatientValidator() {
		super();
	}

	public List<String> validate(PatientRequest patientRequest, boolean isIdRequired) {
		List<String> errors = new ArrayList<>();
		errors.add(validateId(patientRequest.getId(), isIdRequired));
		if (patientRequest.getStateRegisteredNursesIds() == null || patientRequest.getStateRegisteredNursesIds().isEmpty()) {
			errors.add("You must fill in at least one nurse");
		} else {
			for (Long id : patientRequest.getStateRegisteredNursesIds()) {
				errors.add(validateStateRegisteredNurseId(id));
			}
		}
		errors.add(validateFirstName(patientRequest.getFirstName()));
		errors.add(validateLastName(patientRequest.getLastName()));
		errors.removeIf(String::isEmpty);
		return errors;
	}

}
