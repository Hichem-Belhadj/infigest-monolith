package com.hbtheme.infigestback.dto;

import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerInvoiceSimpleResponse {
	private Long id;
	private String customerInvoiceNumber;
	private StateRegisteredNurseSimpleResponse stateRegisteredNurse;
	private BigDecimal amount;
}
