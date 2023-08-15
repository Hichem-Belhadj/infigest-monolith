package com.hbtheme.infigestback.controller;

import com.hbtheme.infigestback.dto.InsuranceRequest;
import com.hbtheme.infigestback.dto.InsuranceResponse;
import com.hbtheme.infigestback.dto.InsuranceSimpleResponse;
import com.hbtheme.infigestback.mapper.InsuranceMapper;
import com.hbtheme.infigestback.service.InsuranceService;
import com.hbtheme.infigestback.tools.Constant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(Constant.INSURANCE_RESOURCE_API)
@RequiredArgsConstructor
@Slf4j
public class InsuranceController extends BaseController {
	
	private final InsuranceService insuranceService;
	
	private final InsuranceMapper insuranceMapper;
	
	@PostMapping
	ResponseEntity<InsuranceResponse> createInvoice(@RequestBody InsuranceRequest insuranceRequest) {

		try {
			insuranceService.saveInsurance(insuranceRequest, false);
			String successMessage = String.format("Insurance with name \"%s\" created successfully!", insuranceRequest.getName());
			log.info(successMessage);
			return new ResponseEntity<>(new InsuranceResponse(successMessage), HttpStatus.CREATED);
		} catch (Exception e) {
			String message = String.format("Error while processing insurance creation (%s)", e.getMessage());
			log.error(message);
			return new ResponseEntity<>(new InsuranceResponse(message), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping
	ResponseEntity<InsuranceResponse> findAllInsurances() {
		try {
			List<InsuranceSimpleResponse> insuranceSimpleResponses = insuranceService.findAllInsurances().stream()
					.map(insuranceMapper::toSimpleResponse).toList();

			return new ResponseEntity<>(InsuranceResponse.builder()
					.message(String.format("Successfully returned %s insurance(s)", insuranceSimpleResponses.size()))
					.insuranceSimpleResponses(insuranceSimpleResponses).build(), HttpStatus.OK);
		} catch (Exception e) {
			String errorMessage = "Failed to complete get all insurance";
			return new ResponseEntity<>(new InsuranceResponse(errorMessage),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping(path = "/byId/{id}")
	ResponseEntity<InsuranceResponse> findInsuranceById(@PathVariable(value = "id") Long id) {
		try {
			List<InsuranceSimpleResponse> insuranceSimpleResponses = insuranceService.findInsuranceById(id).stream()
					.map(insuranceMapper::toSimpleResponse).toList();

			return new ResponseEntity<>(InsuranceResponse.builder()
					.message("Successfully returned 1 insurance")
					.insuranceSimpleResponses(insuranceSimpleResponses).build(), HttpStatus.OK);
		} catch (Exception e) {
			String message = String.format("Failed to complete get insurance (%s)", e.getMessage());
			log.error(message);
			return new ResponseEntity<>(new InsuranceResponse(message),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PutMapping(path = "/update")
	public ResponseEntity<InsuranceResponse> updateInsurance(@RequestBody InsuranceRequest insuranceRequest) {
		try {
			insuranceService.saveInsurance(insuranceRequest, true);
			String successMessage = String.format("Insurance with id \"%s\" updated successfully!", insuranceRequest.getId());
			log.info(successMessage);
			return new ResponseEntity<>(new InsuranceResponse(successMessage), HttpStatus.OK);
		} catch (Exception e) {
			String message = String.format("Error while processing insurance updating! (%s)", e.getMessage());
			log.error(message);
			return new ResponseEntity<>(new InsuranceResponse(message), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@DeleteMapping(path = "/delete/{id}")
	public ResponseEntity<InsuranceResponse> deleteInsuranceById(@PathVariable(value = "id") Long id) {
		try {
			insuranceService.deleteInsuranceById(id);
			String successMessage = String.format("Insurance with id \"%s\" deleted successfully!", id);
			log.info(successMessage);
			return new ResponseEntity<>(new InsuranceResponse(successMessage), HttpStatus.OK);
		} catch (DataIntegrityViolationException e) {
			String message = String.format("You cannot delete this mutual because it has already been used (%s)", e.getMessage());
			log.error(message);
			return new ResponseEntity<>(new InsuranceResponse(message), HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			String message = String.format("Error while processing insurance deleting (%s)", e.getMessage());
			log.error(message);
			return new ResponseEntity<>(new InsuranceResponse(message), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
