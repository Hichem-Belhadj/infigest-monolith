package com.hbtheme.infigestback.service.validator;

import com.hbtheme.infigestback.dto.CustomerInvoiceRequest;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CustomerInvoiceValidator extends BaseValidator {

	public List<String> validate(CustomerInvoiceRequest customerInvoiceRequest, boolean isIdRequired) {
		List<String> errors = new ArrayList<>();
		errors.add(validateId(customerInvoiceRequest.getId(), isIdRequired));
		errors.add(validateStateRegisteredNurseId(customerInvoiceRequest.getStateRegisteredNurseId()));
		errors.removeIf(String::isEmpty);
		return errors;
	}

}
