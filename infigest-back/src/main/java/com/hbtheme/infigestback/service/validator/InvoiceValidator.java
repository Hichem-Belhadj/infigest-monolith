package com.hbtheme.infigestback.service.validator;

import com.hbtheme.infigestback.dto.InvoiceRequest;
import com.hbtheme.infigestback.service.CustomerInvoiceService;
import com.hbtheme.infigestback.service.PatientService;
import com.hbtheme.infigestback.tools.DateUtils;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class InvoiceValidator extends BaseValidator {

	public InvoiceValidator(PatientService patientService, CustomerInvoiceService customerInvoiceService) {
		super(patientService, customerInvoiceService);
	}

	public List<String> validate(InvoiceRequest invoiceRequest, boolean isIdRequired) {
		List<String> errors = new ArrayList<>();
		errors.add(validateId(invoiceRequest.getId(), isIdRequired));
		errors.add(validateStateRegisteredNurseId(invoiceRequest.getStateRegisteredNurseId()));
		errors.add(validateStateRegisteredNurse(invoiceRequest.getPatientId(), invoiceRequest.getStateRegisteredNurseId()));
		errors.add(validatePatientId(invoiceRequest.getPatientId()));
		errors.add(validateDateFormat(invoiceRequest.getInvoiceDate(), "invoice"));
		errors.add(validateDateFormat(invoiceRequest.getCareStartDate(), "care start date"));
		errors.add(validateDateFormat(invoiceRequest.getCareEndDate(), "care end date"));
		errors.add(validateCareDates(invoiceRequest.getCareStartDate(), invoiceRequest.getCareEndDate()));
		errors.add(validateCustomerInvoice(invoiceRequest.getCustomerInvoiceId(), invoiceRequest.getStateRegisteredNurseId()));
		errors.removeIf(String::isEmpty);
		return errors;
	}

	private String validateCareDates(String careStartDate, String careEndDate) {
		String error = "";
		if (careStartDate == null || careStartDate.isEmpty() || !DateUtils.isValidDate(careStartDate)
				|| careEndDate == null || careEndDate.isEmpty() || !DateUtils.isValidDate(careEndDate)) {
			return error;
		}
		LocalDate careStartLocalDate = DateUtils.parseStringToDate(careStartDate);
		LocalDate careEndLocalDate = DateUtils.parseStringToDate(careEndDate);
		
		if (careStartLocalDate.isAfter(careEndLocalDate)) {
			error = "The start date of care must precede the end date";
		}
		return error;
	}

}
