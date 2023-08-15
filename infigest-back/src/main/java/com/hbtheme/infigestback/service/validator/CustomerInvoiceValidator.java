package com.hbtheme.infigestback.service.validator;

import com.hbtheme.infigestback.dto.CustomerInvoiceRequest;
import com.hbtheme.infigestback.model.CustomerInvoice;
import com.hbtheme.infigestback.model.Invoice;
import com.hbtheme.infigestback.model.InvoiceRejection;
import com.hbtheme.infigestback.service.CustomerInvoiceService;
import com.hbtheme.infigestback.service.PatientService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CustomerInvoiceValidator extends BaseValidator {

	public CustomerInvoiceValidator() {
		super();
	}

	public List<String> validate(CustomerInvoiceRequest customerInvoiceRequest, boolean isIdRequired) {
		List<String> errors = new ArrayList<>();
		errors.add(validateId(customerInvoiceRequest.getId(), isIdRequired));
		errors.add(validateStateRegisteredNurseId(customerInvoiceRequest.getStateRegisteredNurseId()));
		errors.removeIf(String::isEmpty);
		return errors;
	}

}
