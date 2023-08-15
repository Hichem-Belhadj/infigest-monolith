package com.hbtheme.infigestback.controller;

import com.hbtheme.infigestback.dto.InvoiceRequest;
import com.hbtheme.infigestback.dto.InvoiceResponse;
import com.hbtheme.infigestback.dto.InvoiceSimpleResponse;
import com.hbtheme.infigestback.dto.PatientResponse;
import com.hbtheme.infigestback.mapper.InvoiceMapper;
import com.hbtheme.infigestback.service.InvoiceService;
import com.hbtheme.infigestback.tools.Constant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(Constant.INVOICE_RESOURCE_API)
@RequiredArgsConstructor
@Slf4j
public class InvoiceController extends BaseController {
	
	private final InvoiceService invoiceService;
	
	private final InvoiceMapper invoiceMapper;

	@PostMapping
	ResponseEntity<InvoiceResponse> createInvoice(@RequestBody InvoiceRequest invoiceRequest) {

		try {
			invoiceService.saveInvoice(invoiceRequest, false);
			String successMessage = "New Invoice created successfully!";
			log.info(successMessage);
			return new ResponseEntity<>(new InvoiceResponse(successMessage), HttpStatus.CREATED);
		} catch (Exception e) {
			String errorMessage = String.format("Error while processing invoice creation (%s)", e.getMessage());
			log.error(errorMessage);
			return new ResponseEntity<>(new InvoiceResponse(errorMessage), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping
	ResponseEntity<InvoiceResponse> findAllInvoices() {
		try {
			List<InvoiceSimpleResponse> invoiceSimpleResponses = invoiceService.findAllInvoices().stream()
					.map(invoiceMapper::toSimpleResponse).toList();

			return new ResponseEntity<>(InvoiceResponse.builder()
					.message(String.format("Successfully returned %s Invoice(s)", invoiceSimpleResponses.size()))
					.invoiceSimpleResponses(invoiceSimpleResponses).build(), HttpStatus.OK);
		} catch (Exception e) {
			String errorMessage = "Failed to complete get all Invoice";
			return new ResponseEntity<>(new InvoiceResponse(errorMessage),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping(path = "/byId/{id}")
	ResponseEntity<InvoiceResponse> findInvoiceById(@PathVariable(value = "id") Long id) {
		try {
			List<InvoiceSimpleResponse> invoiceSimpleResponses = invoiceService.findInvoiceById(id).stream()
					.map(invoiceMapper::toSimpleResponse).toList();

			return new ResponseEntity<>(InvoiceResponse.builder()
					.message("Successfully returned 1 Invoice")
					.invoiceSimpleResponses(invoiceSimpleResponses).build(), HttpStatus.OK);
		} catch (Exception e) {
			String errorMessage = String.format("Failed to complete get invoice (%s)", e.getMessage());
			log.error(errorMessage);
			return new ResponseEntity<>(new InvoiceResponse(errorMessage),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping(path = "/update")
	public ResponseEntity<InvoiceResponse> updateInvoice(@RequestBody InvoiceRequest invoiceRequest) {
		try {
			invoiceService.saveInvoice(invoiceRequest, true);
			String successMessage = String.format("Invoice with id %s updated successfully!", invoiceRequest.getId());
			log.info(successMessage);
			return new ResponseEntity<>(new InvoiceResponse(successMessage), HttpStatus.OK);
		} catch (Exception e) {
			String errorMessage = String.format("Error while processing invoice updating (%s)", e.getMessage());
			log.error(errorMessage);
			return new ResponseEntity<>(new InvoiceResponse(errorMessage), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping(path = "/delete/{id}")
	public ResponseEntity<InvoiceResponse> deleteInvoiceById(@PathVariable(value = "id") Long id) {
		try {
			invoiceService.deleteInvoiceById(id);
			String successMessage = String.format("Invoice with id %s deleted successfully!", id);
			log.info(successMessage);
			return new ResponseEntity<>(new InvoiceResponse(successMessage), HttpStatus.OK);
		} catch (DataIntegrityViolationException e) {
			String errorMessage = String.format("You cannot delete this invoice as he has already been assigned (%s)", e.getMessage());
			log.error(errorMessage);
			return new ResponseEntity<>(new InvoiceResponse(errorMessage), HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			String errorMessage = String.format("Error while processing invoice deleting (%s)", e.getMessage());
			log.error(errorMessage);
			return new ResponseEntity<>(new InvoiceResponse(errorMessage), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
}
