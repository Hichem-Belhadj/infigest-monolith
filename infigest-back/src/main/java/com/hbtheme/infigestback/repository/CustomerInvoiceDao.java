package com.hbtheme.infigestback.repository;

import com.hbtheme.infigestback.model.CustomerInvoice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerInvoiceDao extends JpaRepository<CustomerInvoice, Long> {
}
