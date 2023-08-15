package com.hbtheme.infigestback.service.validator;

import com.hbtheme.infigestback.dto.InsuranceRequest;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class InsuranceValidator extends BaseValidator {

	public List<String> validate(InsuranceRequest insuranceRequest, boolean isIdRequired) {
		List<String> errors = new ArrayList<>();
		errors.add(validateId(insuranceRequest.getId(), isIdRequired));
		errors.add(validName(insuranceRequest.getName()));
		errors.removeIf(String::isEmpty);
		return errors;
	}

	private String validName(String name) {
		String error = "";
		if (name == null || name.isEmpty()) {
			error = "The insurance name must be submitted";
		}
		return error;
	}

}
