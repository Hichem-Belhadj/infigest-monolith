package com.hbtheme.infigestback.service.impl;

import com.hbtheme.infigestback.dto.CustomerInvoiceRequest;
import com.hbtheme.infigestback.mapper.CustomerInvoiceMapper;
import com.hbtheme.infigestback.model.CustomerInvoice;
import com.hbtheme.infigestback.model.StateRegisteredNurse;
import com.hbtheme.infigestback.repository.CustomerInvoiceDao;
import com.hbtheme.infigestback.service.CustomerInvoiceService;
import com.hbtheme.infigestback.service.StateRegisteredNurseService;
import com.hbtheme.infigestback.service.validator.CustomerInvoiceValidator;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerInvoiceServiceImpl implements CustomerInvoiceService {

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
			findCustomerInvoiceById(customerInvoiceRequest.getId());
		}

		StateRegisteredNurse stateRegisteredNurse = stateRegisteredNurseService
				.findStateRegisteredNurseById(customerInvoiceRequest.getStateRegisteredNurseId()).get(0);

		CustomerInvoice customerInvoice = customerInvoiceMapper.toModel(customerInvoiceRequest, stateRegisteredNurse);
		customerInvoiceDao.save(customerInvoice);
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

}
