package com.hbtheme.infigestback.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
public class CustomerInvoiceResponse extends BaseResponse {
	private List<CustomerInvoiceSimpleResponse> customerInvoiceSimpleResponses;

	public CustomerInvoiceResponse(String message) {
		super();
		this.message = message;
	}
}
