package com.hbtheme.infigestback.service.validator;

import com.hbtheme.infigestback.model.CustomerInvoice;
import com.hbtheme.infigestback.model.Patient;
import com.hbtheme.infigestback.model.StateRegisteredNurse;
import com.hbtheme.infigestback.service.CustomerInvoiceService;
import com.hbtheme.infigestback.service.PatientService;
import com.hbtheme.infigestback.tools.DateUtils;

import java.util.List;

public class BaseValidator {

	private PatientService patientService;
	private CustomerInvoiceService customerInvoiceService;

	public BaseValidator(PatientService patientService, CustomerInvoiceService customerInvoiceService) {
		this.patientService = patientService;
		this.customerInvoiceService = customerInvoiceService;
	}

	public BaseValidator() {
	}

	public BaseValidator(PatientService patientService) {
		this.patientService = patientService;
	}

	protected String validateId(Long id, boolean isIdRequired) {
		String error = "";
		if (isIdRequired && id == null) {
			error = "The id must be submitted";
		}
		return error;
	}
	
	protected String validateStateRegisteredNurseId(Long stateRegisteredNurseId) {
		String error = "";
		if (stateRegisteredNurseId == null) {
			error = "The nurse's id must be submitted";
		}
		return error;
	}

	protected String validateStateRegisteredNurse(Long patientId, Long stateRegisteredNurseId) {
		String error = "";
		Patient patient = patientService.findPatientById(patientId).get(0);
		List<StateRegisteredNurse> stateRegisteredNurseList = patient.getStateRegisteredNurses();
		List<Long> ids = stateRegisteredNurseList.stream()
				.map(StateRegisteredNurse::getId)
				.toList();
		if (!ids.contains(stateRegisteredNurseId)) {
			error = "A selected nurse is not in charge of this patient";
		}
		return error;
	}
	
	protected String validateFirstName(String firstName) {
		String error = "";
		if (firstName == null || firstName.isEmpty()) {
			error = "The first name must be submitted";
		}
		return error;
	}
	
	protected String validateLastName(String lastName) {
		String error = "";
		if (lastName == null || lastName.isEmpty()) {
			error = "The last name must be submitted";
		}
		return error;
	}

	protected String validatePatientId(Long patientId) {
		String error = "";
		if (patientId == null) {
			error = "The patient's id must be submitted";
		}
		return error;
	}

	protected String validateDateFormat(String date, String dateName) {
		String error = "";
		if (date == null || date.isEmpty()) {
			return error;
		}
		if (!DateUtils.isValidDate(date)) {
			error = String.format("The date format is invalid for %s", dateName);
		}
		return error;
	}

	protected String validateCustomerInvoice(Long customerInvoiceId, Long nurseId) {
		String error = "";
		CustomerInvoice customerInvoice = customerInvoiceService.findCustomerInvoiceById(customerInvoiceId).get(0);
		if (!customerInvoice.getId().equals(nurseId)) {
			error = "The nurse who performed the treatment does not match this invoice";
		}
		return error;
	}
	
}
