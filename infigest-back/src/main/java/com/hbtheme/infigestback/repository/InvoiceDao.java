package com.hbtheme.infigestback.repository;

import com.hbtheme.infigestback.model.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceDao extends JpaRepository<Invoice, Long> {
}
