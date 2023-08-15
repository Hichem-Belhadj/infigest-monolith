package com.hbtheme.infigestback.mapper;

import com.hbtheme.infigestback.dto.CustomerInvoiceSimpleResponse;
import com.hbtheme.infigestback.model.CustomerInvoice;
import com.hbtheme.infigestback.service.CustomerInvoiceService;

public class BaseMapper {
    private final CustomerInvoiceMapper customerInvoiceMapper;
    private  final CustomerInvoiceService customerInvoiceService;

    public BaseMapper(CustomerInvoiceMapper customerInvoiceMapper, CustomerInvoiceService customerInvoiceService) {
        this.customerInvoiceMapper = customerInvoiceMapper;
        this.customerInvoiceService = customerInvoiceService;
    }

    CustomerInvoiceSimpleResponse getCustomerInvoiceSimpleResponse(CustomerInvoice customerInvoice) {
        return customerInvoice == null ?
                CustomerInvoiceSimpleResponse.builder().build() :
                customerInvoiceMapper.toSimpleResponse(customerInvoice);
    }

    CustomerInvoice getCustomerInvoice(Long customerInvoiceId) {
        return customerInvoiceId == null ?
                null :
                customerInvoiceService.findCustomerInvoiceById(customerInvoiceId).get(0);
    }
}
