package com.hbtheme.infigestback.repository;

import com.hbtheme.infigestback.model.CustomerInvoice;
import com.hbtheme.infigestback.model.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InvoiceDao extends JpaRepository<Invoice, Long> {
    List<Invoice> findByCustomerInvoiceId(Long customerInvoiceId);
}
