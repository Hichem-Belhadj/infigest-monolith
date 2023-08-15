package com.hbtheme.infigestback.controller;

import com.hbtheme.infigestback.dto.CustomerInvoiceRequest;
import com.hbtheme.infigestback.dto.CustomerInvoiceResponse;
import com.hbtheme.infigestback.dto.CustomerInvoiceSimpleResponse;
import com.hbtheme.infigestback.dto.PatientResponse;
import com.hbtheme.infigestback.mapper.CustomerInvoiceMapper;
import com.hbtheme.infigestback.service.CustomerInvoiceService;
import com.hbtheme.infigestback.tools.Constant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(Constant.CUSTOMER_INVOICE_RESOURCE_API)
@RequiredArgsConstructor
@Slf4j
public class CustomerInvoiceController extends BaseController {
	
	private final CustomerInvoiceService customerInvoiceService;
	
	private final CustomerInvoiceMapper customerInvoiceMapper;

	@PostMapping
	ResponseEntity<CustomerInvoiceResponse> createCustomerInvoice(@RequestBody CustomerInvoiceRequest customerInvoiceRequest) {

		try {
			customerInvoiceService.saveCustomerInvoice(customerInvoiceRequest, false);
			String successMessage = "Customer invoice created successfully!";
			log.info(successMessage);
			return new ResponseEntity<>(new CustomerInvoiceResponse(successMessage), HttpStatus.CREATED);
		} catch (Exception e) {
			String errorMessage = String.format("Error while processing customer invoice creation (%s)", e.getMessage());
			log.error(errorMessage);
			return new ResponseEntity<>(new CustomerInvoiceResponse(errorMessage), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping
	ResponseEntity<CustomerInvoiceResponse> findAllCustomerInvoices() {
		try {
			List<CustomerInvoiceSimpleResponse> customerInvoiceSimpleResponses = customerInvoiceService.findAllCustomerInvoices().stream()
					.map(customerInvoiceMapper::toSimpleResponse).toList();

			return new ResponseEntity<>(CustomerInvoiceResponse.builder()
					.message(String.format("Successfully returned %s customer invoice(s)", customerInvoiceSimpleResponses.size()))
					.customerInvoiceSimpleResponses(customerInvoiceSimpleResponses).build(), HttpStatus.OK);
		} catch (Exception e) {
			String errorMessage = "Failed to complete get all customer invoices";
			return new ResponseEntity<>(new CustomerInvoiceResponse(errorMessage),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping(path = "/byId/{id}")
	ResponseEntity<CustomerInvoiceResponse> findCustomerInvoiceById(@PathVariable(value = "id") Long id) {
		try {
			List<CustomerInvoiceSimpleResponse> customerInvoiceSimpleResponses = customerInvoiceService.findCustomerInvoiceById(id).stream()
					.map(customerInvoiceMapper::toSimpleResponse).toList();

			return new ResponseEntity<>(CustomerInvoiceResponse.builder()
					.message("Successfully returned 1 customer invoice")
					.customerInvoiceSimpleResponses(customerInvoiceSimpleResponses).build(), HttpStatus.OK);
		} catch (Exception e) {
			String errorMessage = String.format("Failed to complete get invoice (%s)", e.getMessage());
			log.error(errorMessage);
			return new ResponseEntity<>(new CustomerInvoiceResponse(errorMessage),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PutMapping(path = "/update")
	public ResponseEntity<CustomerInvoiceResponse> updateCustomerInvoice(@RequestBody CustomerInvoiceRequest customerInvoiceRequest) {
		try {
			customerInvoiceService.saveCustomerInvoice(customerInvoiceRequest, true);
			String successMessage = String.format("Customer invoice with id %s updated successfully!", customerInvoiceRequest.getId());
			log.info(successMessage);
			return new ResponseEntity<>(new CustomerInvoiceResponse(successMessage), HttpStatus.OK);
		} catch (Exception e) {
			String errorMessage = String.format("Error while processing customer invoice updating (%s)", e.getMessage());
			log.error(errorMessage);
			return new ResponseEntity<>(new CustomerInvoiceResponse(errorMessage), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@DeleteMapping(path = "/delete/{id}")
	public ResponseEntity<CustomerInvoiceResponse> deleteCustomerInvoiceById(@PathVariable(value = "id") Long id) {
		try {
			customerInvoiceService.deleteCustomerInvoiceById(id);
			String successMessage = String.format("Customer invoice with id %s deleted successfully!", id);
			log.info(successMessage);
			return new ResponseEntity<>(new CustomerInvoiceResponse(successMessage), HttpStatus.OK);
		} catch (DataIntegrityViolationException e) {
			String errorMessage = String.format("You cannot delete this customer invoice as he has already been assigned (%s)", e.getMessage());
			log.error(errorMessage);
			return new ResponseEntity<>(new CustomerInvoiceResponse(errorMessage), HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			String errorMessage = String.format("Error while processing customer invoice deleting (%s)", e.getMessage());
			log.error(errorMessage);
			return new ResponseEntity<>(new CustomerInvoiceResponse(errorMessage), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
}
