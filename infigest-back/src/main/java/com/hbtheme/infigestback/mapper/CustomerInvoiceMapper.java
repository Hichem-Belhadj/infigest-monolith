package com.hbtheme.infigestback.mapper;

import com.hbtheme.infigestback.dto.CustomerInvoiceRequest;
import com.hbtheme.infigestback.dto.CustomerInvoiceSimpleResponse;
import com.hbtheme.infigestback.model.CustomerInvoice;
import com.hbtheme.infigestback.model.StateRegisteredNurse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerInvoiceMapper {

	private final StateRegisteredNurseMapper stateRegisteredNurseMapper;

	public CustomerInvoiceSimpleResponse toSimpleResponse(CustomerInvoice customerInvoice) {
		return CustomerInvoiceSimpleResponse.builder()
				.id(customerInvoice.getId())
				.customerInvoiceNumber(customerInvoice.getCustomerInvoiceNumber())
				.stateRegisteredNurse(stateRegisteredNurseMapper.toSimpleResponse(customerInvoice.getStateRegisteredNurse()))
				.amount(customerInvoice.getAmount())
				.build();
	}

	public CustomerInvoice toModel(CustomerInvoiceRequest customerInvoiceRequest,
								   StateRegisteredNurse stateRegisteredNurse) {
		return CustomerInvoice.builder()
				.id(customerInvoiceRequest.getId())
				.customerInvoiceNumber(customerInvoiceRequest.getCustomerInvoiceNumber())
				.stateRegisteredNurse(stateRegisteredNurse)
				.build();
	}

}
