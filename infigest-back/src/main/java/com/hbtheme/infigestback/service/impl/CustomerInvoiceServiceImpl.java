package com.hbtheme.infigestback.service.impl;

import com.hbtheme.infigestback.dto.CustomerInvoiceRequest;
import com.hbtheme.infigestback.mapper.CustomerInvoiceMapper;
import com.hbtheme.infigestback.model.CustomerInvoice;
import com.hbtheme.infigestback.model.Invoice;
import com.hbtheme.infigestback.model.InvoiceRejection;
import com.hbtheme.infigestback.model.StateRegisteredNurse;
import com.hbtheme.infigestback.repository.CustomerInvoiceDao;
import com.hbtheme.infigestback.repository.InvoiceDao;
import com.hbtheme.infigestback.repository.InvoiceRejectionDao;
import com.hbtheme.infigestback.service.CustomerInvoiceService;
import com.hbtheme.infigestback.service.StateRegisteredNurseService;
import com.hbtheme.infigestback.service.validator.CustomerInvoiceValidator;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerInvoiceServiceImpl implements CustomerInvoiceService {

	private final InvoiceDao invoiceDao;

	private final InvoiceRejectionDao invoiceRejectionDao;

	private final CustomerInvoiceValidator customerInvoiceValidator;

	private final StateRegisteredNurseService stateRegisteredNurseService;

	private final CustomerInvoiceMapper customerInvoiceMapper;

	private final CustomerInvoiceDao customerInvoiceDao;

	@Override
	public void saveCustomerInvoice(CustomerInvoiceRequest customerInvoiceRequest, boolean isIdRequired) {
		if (customerInvoiceRequest == null) {
			throw new IllegalArgumentException();
		}

		List<String> errors = customerInvoiceValidator.validate(customerInvoiceRequest, isIdRequired);
		if (!errors.isEmpty()) {
			throw new IllegalStateException(String.join("; ", errors));
		}

		if (isIdRequired) {
			CustomerInvoice customerInvoice = findCustomerInvoiceById(customerInvoiceRequest.getId()).get(0);
			if (!CheckNurseConsistency(customerInvoice, customerInvoiceRequest.getStateRegisteredNurseId())) {
				throw  new IllegalArgumentException("You cannot modify the nurse. This invoice contains treatments performed by this nurse");
			}
		}

		StateRegisteredNurse stateRegisteredNurse = stateRegisteredNurseService
				.findStateRegisteredNurseById(customerInvoiceRequest.getStateRegisteredNurseId()).get(0);

		CustomerInvoice customerInvoice = customerInvoiceMapper.toModel(customerInvoiceRequest, stateRegisteredNurse);
		customerInvoiceDao.save(customerInvoice);
	}

	private boolean CheckNurseConsistency(CustomerInvoice customerInvoice, long stateRegisteredNurseId) {
		List<Invoice> invoices = customerInvoice.getInvoices();
		List<InvoiceRejection> invoiceRejections = customerInvoice.getInvoiceRejections();

		if (invoices.isEmpty() && invoiceRejections.isEmpty()) {
			return true;
		}

		return (invoices.isEmpty() || invoices.get(0).getStateRegisteredNurse().getId().equals(stateRegisteredNurseId))
				&& (invoiceRejections.isEmpty() || invoiceRejections.get(0).getStateRegisteredNurse().getId().equals(stateRegisteredNurseId));
	}

	@Override
	public List<CustomerInvoice> findAllCustomerInvoices() {
		return customerInvoiceDao.findAll();
	}

	@Override
	public List<CustomerInvoice> findCustomerInvoiceById(Long id) {
		if (id == null) {
			throw new IllegalArgumentException();
		}
		List<CustomerInvoice> customerInvoices = new ArrayList<>();
		CustomerInvoice customerInvoice = customerInvoiceDao.findById(id).orElseThrow(() ->
				new EntityNotFoundException("Customer invoice not found"));
		customerInvoices.add(customerInvoice);
		return customerInvoices;
	}

	@Override
	public void deleteCustomerInvoiceById(Long id) {
		if (id == null || findCustomerInvoiceById(id).isEmpty()) {
			throw new IllegalArgumentException();
		}
		findCustomerInvoiceById(id);
		customerInvoiceDao.deleteById(id);
	}

	@Override
	public void increaseCustomerInvoiceAmount(Long customerInvoiceId) {
		CustomerInvoice customerInvoice = findCustomerInvoiceById(customerInvoiceId).get(0);
		List<Invoice> invoices = invoiceDao.findByCustomerInvoiceId(customerInvoiceId);
		List<InvoiceRejection> invoiceRejections = invoiceRejectionDao.findByCustomerInvoiceId(customerInvoiceId);

		BigDecimal invoicesAmount = invoices.stream()
				.map(Invoice::getTotalBilled)
				.reduce(BigDecimal.ZERO, BigDecimal::add);

		BigDecimal invoiceRejectionsAmount = invoiceRejections.stream()
				.map(InvoiceRejection::getAmount)
				.reduce(BigDecimal.ZERO, BigDecimal::add);
		customerInvoice.setAmount(invoicesAmount.add(invoiceRejectionsAmount));
		customerInvoiceDao.save(customerInvoice);
	}

}
