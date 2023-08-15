package com.hbtheme.infigestback.service;

import com.hbtheme.infigestback.dto.InvoiceRequest;
import com.hbtheme.infigestback.model.Invoice;

import java.util.List;

public interface InvoiceService {

	void saveInvoice(InvoiceRequest invoiceRequest, boolean isIdRequired);

	List<Invoice> findAllInvoices();

	List<Invoice> findInvoiceById(Long id);

	void deleteInvoiceById(Long id);

}
