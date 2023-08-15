package com.hbtheme.infigestback.controller;

import com.hbtheme.infigestback.dto.InvoiceRejectionRequest;
import com.hbtheme.infigestback.dto.InvoiceRejectionResponse;
import com.hbtheme.infigestback.dto.InvoiceRejectionSimpleResponse;
import com.hbtheme.infigestback.mapper.InvoiceRejectionMapper;
import com.hbtheme.infigestback.service.InvoiceRejectionService;
import com.hbtheme.infigestback.tools.Constant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(Constant.INVOICE_REJECTION_RESOURCE_API)
@RequiredArgsConstructor
@Slf4j
@Transactional
public class InvoiceRejectionController extends BaseController {
	
	private final InvoiceRejectionService invoiceRejectionService;
	
	private final InvoiceRejectionMapper invoiceRejectionMapper;
	
	@PostMapping
	ResponseEntity<InvoiceRejectionResponse> createInvoiceRejection(@RequestBody InvoiceRejectionRequest invoiceRejectionRequest) {

		try {
			invoiceRejectionService.saveInvoiceRejection(invoiceRejectionRequest, false);
			String successMessage = "New Invoice rejection created successfully!";
			log.info(successMessage);
			return new ResponseEntity<>(new InvoiceRejectionResponse(successMessage), HttpStatus.CREATED);
		} catch (Exception e) {
			String errorMessage = String.format("Error while processing invoice rejection creation (%s)", e.getMessage());
			log.error(errorMessage);
			return new ResponseEntity<>(new InvoiceRejectionResponse(errorMessage), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping
	ResponseEntity<InvoiceRejectionResponse> findAllInvoiceRejections() {
		try {
			List<InvoiceRejectionSimpleResponse> invoiceRejectionSimpleResponses = invoiceRejectionService.findAllInvoiceRejections().stream()
					.map(invoiceRejectionMapper::toSimpleResponse).toList();

			return new ResponseEntity<>(InvoiceRejectionResponse.builder()
					.message(String.format("Successfully returned %s invoice rejection(s)", invoiceRejectionSimpleResponses.size()))
					.invoiceRejectionSimpleResponses(invoiceRejectionSimpleResponses).build(), HttpStatus.OK);
		} catch (Exception e) {
			String errorMessage = "Failed to complete get all invoice rejection";
			return new ResponseEntity<>(new InvoiceRejectionResponse(errorMessage),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping(path = "/byId/{id}")
	ResponseEntity<InvoiceRejectionResponse> findInvoiceRejectionById(@PathVariable(value = "id") Long id) {
		try {
			List<InvoiceRejectionSimpleResponse> invoiceRejectionSimpleResponses = invoiceRejectionService.findInvoiceRejectionById(id).stream()
					.map(invoiceRejectionMapper::toSimpleResponse).toList();

			return new ResponseEntity<>(InvoiceRejectionResponse.builder()
					.message("Successfully returned 1 invoice rejection")
					.invoiceRejectionSimpleResponses(invoiceRejectionSimpleResponses).build(), HttpStatus.OK);
		} catch (Exception e) {
			String errorMessage = String.format("Failed to complete get invoice rejection (%s)", e.getMessage());
			log.error(errorMessage);
			return new ResponseEntity<>(new InvoiceRejectionResponse(errorMessage),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping(path = "/update")
	public ResponseEntity<InvoiceRejectionResponse> updateInvoiceRejection(@RequestBody InvoiceRejectionRequest invoiceRejectionRequest) {
		try {
			invoiceRejectionService.saveInvoiceRejection(invoiceRejectionRequest, true);
			String successMessage = String.format("New Invoice Rejection with id %s updated successfully!",
					invoiceRejectionRequest.getId());
			log.info(successMessage);
			return new ResponseEntity<>(new InvoiceRejectionResponse(successMessage), HttpStatus.OK);
		} catch (Exception e) {
			String errorMessage = String.format("Error while processing invoice rejection updating (%s)", e.getMessage());
			log.error(errorMessage);
			return new ResponseEntity<>(new InvoiceRejectionResponse(errorMessage), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping(path = "/delete/{id}")
	public ResponseEntity<InvoiceRejectionResponse> deleteInvoiceRejectionById(@PathVariable(value = "id") Long id) {
		try {
			invoiceRejectionService.deleteInvoiceRejectionById(id);
			String successMessage = String.format("Invoice Rejection with id %s deleted successfully!", id);
			log.info(successMessage);
			return new ResponseEntity<>(new InvoiceRejectionResponse(successMessage), HttpStatus.OK);
		} catch (Exception e) {
			String errorMessage = String.format("Error while processing invoice rejection deleting (%s)", e.getMessage());
			log.error(errorMessage);
			return new ResponseEntity<>(new InvoiceRejectionResponse(errorMessage), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
