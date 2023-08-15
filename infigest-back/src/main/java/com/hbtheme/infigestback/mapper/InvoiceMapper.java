package com.hbtheme.infigestback.mapper;

import com.hbtheme.infigestback.dto.InvoiceRequest;
import com.hbtheme.infigestback.dto.InvoiceSimpleResponse;
import com.hbtheme.infigestback.model.Invoice;
import com.hbtheme.infigestback.model.Patient;
import com.hbtheme.infigestback.model.StateRegisteredNurse;
import com.hbtheme.infigestback.service.CustomerInvoiceService;
import com.hbtheme.infigestback.tools.DateUtils;
import org.springframework.stereotype.Service;

@Service
public class InvoiceMapper extends BaseMapper {

	private final PatientMapper patientMapper;
	private final StateRegisteredNurseMapper stateRegisteredNurseMapper;

	public InvoiceMapper(CustomerInvoiceMapper customerInvoiceMapper, StateRegisteredNurseMapper stateRegisteredNurseMapper,
								  PatientMapper patientMapper, CustomerInvoiceService customerInvoiceService) {
		super(customerInvoiceMapper, customerInvoiceService);
		this.patientMapper = patientMapper;
		this.stateRegisteredNurseMapper = stateRegisteredNurseMapper;
	}

	public Invoice toModel(InvoiceRequest invoiceRequest, StateRegisteredNurse stateRegisteredNurse, Patient patient) {
		return Invoice.builder()
				.id(invoiceRequest.getId())
				.stateRegisteredNurse(stateRegisteredNurse)
				.patient(patient)
				.batchNumber(invoiceRequest.getBatchNumber())
				.patientInvoiceNumber(invoiceRequest.getPatientInvoiceNumber())
				.invoiceDate(DateUtils.parseStringToDate(invoiceRequest.getInvoiceDate()))
				.careStartDate(DateUtils.parseStringToDate(invoiceRequest.getCareStartDate()))
				.careEndDate(DateUtils.parseStringToDate(invoiceRequest.getCareEndDate()))
				.totalBilled(invoiceRequest.getCpamPayment().add(invoiceRequest.getInsurancePayment().add(invoiceRequest.getPatientShare())))
				.rejectionFeedback(invoiceRequest.getRejectionFeedback())
				.cpamPayment(invoiceRequest.getCpamPayment())
				.insurancePayment(invoiceRequest.getInsurancePayment())
				.patientShare(invoiceRequest.getPatientShare())
				.comment(invoiceRequest.getComment())
				.customerInvoice(getCustomerInvoice(invoiceRequest.getCustomerInvoiceId()))
				.isArchived(invoiceRequest.isArchived()).build();
	}

	public InvoiceSimpleResponse toSimpleResponse(Invoice invoice) {
		return InvoiceSimpleResponse.builder()
				.id(invoice.getId())
				.stateRegisteredNurse(stateRegisteredNurseMapper.toSimpleResponse(invoice.getStateRegisteredNurse()))
				.patient(patientMapper.toSimpleResponse(invoice.getPatient()))
				.batchNumber(invoice.getBatchNumber())
				.patientInvoiceNumber(invoice.getPatientInvoiceNumber())
				.invoiceDate(invoice.getInvoiceDate())
				.careStartDate(invoice.getCareStartDate())
				.careEndDate(invoice.getCareEndDate())
				.totalBilled(invoice.getTotalBilled())
				.rejectionFeedback(invoice.getRejectionFeedback())
				.cpamPayment(invoice.getCpamPayment())
				.insurancePayment(invoice.getInsurancePayment())
				.patientShare(invoice.getPatientShare())
				.comment(invoice.getComment())
				.customerInvoice(getCustomerInvoiceSimpleResponse(invoice.getCustomerInvoice()))
				.isArchived(invoice.isArchived()).build();
	}

}
