package com.hbtheme.infigestback.controller;

import com.hbtheme.infigestback.dto.PatientRequest;
import com.hbtheme.infigestback.dto.PatientResponse;
import com.hbtheme.infigestback.dto.PatientSimpleResponse;
import com.hbtheme.infigestback.mapper.PatientMapper;
import com.hbtheme.infigestback.service.PatientService;
import com.hbtheme.infigestback.tools.Constant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(Constant.PATIENT_RESOURCE_API)
@RequiredArgsConstructor
@Slf4j
public class PatientController extends BaseController {

	private final PatientService patientService;

	private final PatientMapper patientMapper;

	@PostMapping
	ResponseEntity<PatientResponse> createPatient(@RequestBody PatientRequest patientRequest) {

		try {
			patientService.savePatient(patientRequest, false);
			String successMessage = String.format("Patient \"%s %S\" created successfully!",
					patientRequest.getFirstName(), patientRequest.getLastName());
			log.info(successMessage);
			return new ResponseEntity<>(new PatientResponse(successMessage), HttpStatus.CREATED);
		} catch (Exception e) {
			String errorMessage = String.format("Error while processing patient creation (%s)", e.getMessage());
			log.error(errorMessage);
			return new ResponseEntity<>(new PatientResponse(errorMessage), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping
	ResponseEntity<PatientResponse> findAllPatients() {
		try {
			List<PatientSimpleResponse> patientSimpleResponses = patientService.findAllPatients().stream()
					.map(patientMapper::toSimpleResponse).toList();

			return new ResponseEntity<>(PatientResponse.builder()
					.message(String.format("Successfully returned %s Patient(s)", patientSimpleResponses.size()))
					.patientSimpleResponses(patientSimpleResponses).build(), HttpStatus.OK);
		} catch (Exception e) {
			String errorMessage = "Failed to complete get all patients";
			return new ResponseEntity<>(new PatientResponse(errorMessage), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping(path = "/byId/{id}")
	ResponseEntity<PatientResponse> findPatientById(@PathVariable(value = "id") Long id) {
		try {
			List<PatientSimpleResponse> patientSimpleResponses = patientService.findPatientById(id).stream()
					.map(patientMapper::toSimpleResponse).toList();

			return new ResponseEntity<>(PatientResponse.builder().message("Successfully returned 1 patient")
					.patientSimpleResponses(patientSimpleResponses).build(), HttpStatus.OK);
		} catch (Exception e) {
			String errorMessage = String.format("Failed to complete get patient (%s)", e.getMessage());
			log.error(errorMessage);
			return new ResponseEntity<>(new PatientResponse(errorMessage), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping(path = "/update")
	public ResponseEntity<PatientResponse> updatePatient(@RequestBody PatientRequest patientRequest) {
		try {
			patientService.savePatient(patientRequest, true);
			String successMessage = String.format("Patient with id %s updated successfully!", patientRequest.getId());
			log.info(successMessage);
			return new ResponseEntity<>(new PatientResponse(successMessage), HttpStatus.OK);
		} catch (Exception e) {
			String errorMessage = String.format("Error while processing patient updating (%s)", e.getMessage());
			log.error(errorMessage);
			return new ResponseEntity<>(new PatientResponse(errorMessage), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping(path = "/delete/{id}")
	public ResponseEntity<PatientResponse> deletePatientById(@PathVariable(value = "id") Long id) {
		try {
			patientService.deletePatientById(id);
			String successMessage = String.format("Patient with id %s deleted successfully!", id);
			log.info(successMessage);
			return new ResponseEntity<>(new PatientResponse(successMessage), HttpStatus.OK);
		} catch (DataIntegrityViolationException e) {
			String errorMessage = String.format("You cannot delete this patient as he has already been assigned (%s)", e.getMessage());
			log.error(errorMessage);
			return new ResponseEntity<>(new PatientResponse(errorMessage), HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			String errorMessage = String.format("Error while processing patient deleting (%s)", e.getMessage());
			log.error(errorMessage);
			return new ResponseEntity<>(new PatientResponse(errorMessage), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
