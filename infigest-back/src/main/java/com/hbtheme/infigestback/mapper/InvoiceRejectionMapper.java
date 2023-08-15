package com.hbtheme.infigestback.mapper;

import com.hbtheme.infigestback.dto.*;
import com.hbtheme.infigestback.model.InvoiceRejection;
import com.hbtheme.infigestback.model.Patient;
import com.hbtheme.infigestback.model.StateRegisteredNurse;
import com.hbtheme.infigestback.service.CustomerInvoiceService;
import com.hbtheme.infigestback.tools.DateUtils;
import org.springframework.stereotype.Service;

@Service
public class InvoiceRejectionMapper extends BaseMapper {

	private final PatientMapper patientMapper;

	public InvoiceRejectionMapper(CustomerInvoiceMapper customerInvoiceMapper,
								  PatientMapper patientMapper, CustomerInvoiceService customerInvoiceService) {
		super(customerInvoiceMapper, customerInvoiceService);
		this.patientMapper = patientMapper;
	}

	public InvoiceRejectionSimpleResponse toSimpleResponse(InvoiceRejection invoiceRejection) {
		return InvoiceRejectionSimpleResponse.builder()
				.id(invoiceRejection.getId())
				.patient(patientMapper.toSimpleResponse(invoiceRejection.getPatient()))
				.amount(invoiceRejection.getAmount())
				.rejectionReason(invoiceRejection.getRejectionReason())
				.feedBackMethod(invoiceRejection.getFeedBackMethod())
				.feedBackDate(invoiceRejection.getFeedBackDate())
				.paymentDate(invoiceRejection.getPaymentDate())
				.softwareScoring(invoiceRejection.isSoftwareScoring())
				.comment(invoiceRejection.getComment())
				.customerInvoice(getCustomerInvoiceSimpleResponse(invoiceRejection.getCustomerInvoice()))
				.isArchived(invoiceRejection.isArchived())
				.build();
	}

	public InvoiceRejection toModel(InvoiceRejectionRequest invoiceRejectionRequest,
									StateRegisteredNurse stateRegisteredNurse, Patient patient) {
		return InvoiceRejection.builder()
				.id(invoiceRejectionRequest.getId())
				.stateRegisteredNurse(stateRegisteredNurse)
				.patient(patient)
				.amount(invoiceRejectionRequest.getAmount())
				.insurance(patient.getInsurance())
				.rejectionReason(invoiceRejectionRequest.getRejectionReason())
				.feedBackMethod(invoiceRejectionRequest.getFeedBackMethod())
				.feedBackDate(DateUtils.parseStringToDate(invoiceRejectionRequest.getFeedBackDate()))
				.paymentDate(DateUtils.parseStringToDate(invoiceRejectionRequest.getPaymentDate()))
				.softwareScoring(invoiceRejectionRequest.isSoftwareScoring())
				.comment(invoiceRejectionRequest.getComment())
				.customerInvoice(getCustomerInvoice(invoiceRejectionRequest.getCustomerInvoiceId()))
				.isArchived(invoiceRejectionRequest.isArchived())
				.build();
	}

}
