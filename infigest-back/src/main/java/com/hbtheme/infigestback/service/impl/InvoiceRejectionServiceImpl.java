package com.hbtheme.infigestback.service.impl;

import com.hbtheme.infigestback.dto.InvoiceRejectionRequest;
import com.hbtheme.infigestback.mapper.InvoiceRejectionMapper;
import com.hbtheme.infigestback.model.InvoiceRejection;
import com.hbtheme.infigestback.model.Patient;
import com.hbtheme.infigestback.model.StateRegisteredNurse;
import com.hbtheme.infigestback.repository.InvoiceRejectionDao;
import com.hbtheme.infigestback.service.InvoiceRejectionService;
import com.hbtheme.infigestback.service.PatientService;
import com.hbtheme.infigestback.service.StateRegisteredNurseService;
import com.hbtheme.infigestback.service.validator.InvoiceRejectionValidator;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InvoiceRejectionServiceImpl implements InvoiceRejectionService {
	
	private final InvoiceRejectionDao invoiceRejectionDao;
	
	private final InvoiceRejectionValidator invoiceRejectionValidator;

	private final StateRegisteredNurseService stateRegisteredNurseService;
	
	private final PatientService patientService;
	
	private final InvoiceRejectionMapper invoiceRejectionMapper;

	@Override
	public void saveInvoiceRejection(InvoiceRejectionRequest invoiceRejectionRequest, boolean isIdRequired) {
		if (invoiceRejectionRequest == null) {
			throw new IllegalArgumentException();
		}
		
		List<String> errors = invoiceRejectionValidator.validate(invoiceRejectionRequest, isIdRequired);
		if (!errors.isEmpty()) {
			throw new IllegalStateException(String.join("; ", errors));
		}

		if (isIdRequired) {
			findInvoiceRejectionById(invoiceRejectionRequest.getId());
		}
		
		StateRegisteredNurse stateRegisteredNurse = stateRegisteredNurseService.findStateRegisteredNurseById(invoiceRejectionRequest.getStateRegisteredNurseId()).get(0);
		Patient patient = patientService.findPatientById(invoiceRejectionRequest.getPatientId()).get(0);
		
		InvoiceRejection invoiceRejection = invoiceRejectionMapper.toModel(invoiceRejectionRequest, stateRegisteredNurse, patient);
		invoiceRejectionDao.save(invoiceRejection);
		
	}

	@Override
	public List<InvoiceRejection> findAllInvoiceRejections() {
		return invoiceRejectionDao.findAll();
	}

	@Override
	public List<InvoiceRejection> findInvoiceRejectionById(Long id) {
		if (id == null) {
			throw new IllegalArgumentException();
		}
		List<InvoiceRejection> invoiceRejections = new ArrayList<>();
		InvoiceRejection invoiceRejection = invoiceRejectionDao.findById(id).orElseThrow(() -> new EntityNotFoundException("Invoice Rejection not found"));
		invoiceRejections.add(invoiceRejection);
		return invoiceRejections;
	}

	@Override
	public void deleteInvoiceRejectionById(Long id) {
		if (id == null || findInvoiceRejectionById(id).isEmpty()) {
			throw new IllegalArgumentException();
		}
		findInvoiceRejectionById(id);
		invoiceRejectionDao.deleteById(id);
	}

}
