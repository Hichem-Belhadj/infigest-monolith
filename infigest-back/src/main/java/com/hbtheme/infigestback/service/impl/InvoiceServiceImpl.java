package com.hbtheme.infigestback.service.impl;

import com.hbtheme.infigestback.dto.InvoiceRequest;
import com.hbtheme.infigestback.mapper.InvoiceMapper;
import com.hbtheme.infigestback.model.Invoice;
import com.hbtheme.infigestback.model.InvoiceRejection;
import com.hbtheme.infigestback.model.Patient;
import com.hbtheme.infigestback.model.StateRegisteredNurse;
import com.hbtheme.infigestback.repository.InvoiceDao;
import com.hbtheme.infigestback.repository.InvoiceRejectionDao;
import com.hbtheme.infigestback.service.CustomerInvoiceService;
import com.hbtheme.infigestback.service.InvoiceService;
import com.hbtheme.infigestback.service.PatientService;
import com.hbtheme.infigestback.service.StateRegisteredNurseService;
import com.hbtheme.infigestback.service.validator.InvoiceValidator;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class InvoiceServiceImpl implements InvoiceService {
	
	private final InvoiceDao invoiceDao;

	private final CustomerInvoiceService customerInvoiceService;

	private final InvoiceValidator invoiceValidator;

	private final StateRegisteredNurseService stateRegisteredNurseService;
	
	private final PatientService patientService;
	
	private final InvoiceMapper invoiceMapper;

	@Override
	public void saveInvoice(InvoiceRequest invoiceRequest, boolean isIdRequired) {
		if (invoiceRequest == null) {
			throw new IllegalArgumentException();
		}
		
		List<String> errors = invoiceValidator.validate(invoiceRequest, isIdRequired);
		if (!errors.isEmpty()) {
			throw new IllegalStateException(String.join("; ", errors));
		}

		if (isIdRequired) {
			findInvoiceById(invoiceRequest.getId());
		}
		
		StateRegisteredNurse stateRegisteredNurse = stateRegisteredNurseService.findStateRegisteredNurseById(invoiceRequest.getStateRegisteredNurseId()).get(0);
		Patient patient = patientService.findPatientById(invoiceRequest.getPatientId()).get(0);

		Invoice invoice = invoiceMapper.toModel(invoiceRequest, stateRegisteredNurse, patient);
		invoiceDao.save(invoice);
		customerInvoiceService.increaseCustomerInvoiceAmount(invoiceRequest.getCustomerInvoiceId());
	}

	@Override
	public List<Invoice> findAllInvoices() {
		return invoiceDao.findAll();
	}

	@Override
	public List<Invoice> findInvoiceById(Long id) {
		if (id == null) {
			throw new IllegalArgumentException();
		}
		List<Invoice> invoices = new ArrayList<>();
		Invoice invoice = invoiceDao.findById(id).orElseThrow(() -> new EntityNotFoundException("Invoice not found"));
		invoices.add(invoice);
		return invoices;
	}

	@Override
	public void deleteInvoiceById(Long id) {
		if (id == null || findInvoiceById(id).isEmpty()) {
			throw new IllegalArgumentException();
		}
		Invoice invoice = findInvoiceById(id).get(0);
		invoiceDao.deleteById(id);
		customerInvoiceService.increaseCustomerInvoiceAmount(invoice.getCustomerInvoice().getId());
	}

}
