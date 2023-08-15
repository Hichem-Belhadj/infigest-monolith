package com.hbtheme.infigestback.service.validator;

import com.hbtheme.infigestback.dto.StateRegisteredNurseRequest;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class StateRegisteredNurseValidator extends BaseValidator {

	public List<String> validate(StateRegisteredNurseRequest stateRegisteredNurseRequest, boolean isIdRequired) {
		List<String> errors = new ArrayList<>();
		errors.add(validateId(stateRegisteredNurseRequest.getId(), isIdRequired));
		errors.add(validateFirstName(stateRegisteredNurseRequest.getFirstName()));
		errors.add(validateLastName(stateRegisteredNurseRequest.getLastName()));
		errors.removeIf(String::isEmpty);
		return errors;
	}
	
}
