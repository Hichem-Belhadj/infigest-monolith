package com.hbtheme.infigestback.service;

import com.hbtheme.infigestback.dto.InvoiceRejectionRequest;
import com.hbtheme.infigestback.model.InvoiceRejection;

import java.util.List;

public interface InvoiceRejectionService {

	void saveInvoiceRejection(InvoiceRejectionRequest invoiceRejectionRequest, boolean isIdRequired);

	List<InvoiceRejection> findAllInvoiceRejections();

	List<InvoiceRejection> findInvoiceRejectionById(Long id);

	void deleteInvoiceRejectionById(Long id);

}
