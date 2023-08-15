package com.hbtheme.infigestback.repository;

import com.hbtheme.infigestback.model.InvoiceRejection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InvoiceRejectionDao extends JpaRepository<InvoiceRejection, Long> {
    List<InvoiceRejection> findByCustomerInvoiceId(Long customerInvoiceId);
}
