package com.hbtheme.infigestback.service;

import com.hbtheme.infigestback.dto.CustomerInvoiceRequest;
import com.hbtheme.infigestback.model.CustomerInvoice;

import java.util.List;

public interface CustomerInvoiceService {

	void saveCustomerInvoice(CustomerInvoiceRequest customerInvoiceRequest, boolean isIdRequired);

	List<CustomerInvoice> findCustomerInvoiceById(Long id);

	List<CustomerInvoice> findAllCustomerInvoices();

	void deleteCustomerInvoiceById(Long id);

	void increaseCustomerInvoiceAmount(Long customerInvoiceId);

}
