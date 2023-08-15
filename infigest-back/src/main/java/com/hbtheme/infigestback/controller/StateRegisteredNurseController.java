package com.hbtheme.infigestback.controller;

import com.hbtheme.infigestback.dto.StateRegisteredNurseRequest;
import com.hbtheme.infigestback.dto.StateRegisteredNurseResponse;
import com.hbtheme.infigestback.dto.StateRegisteredNurseSimpleResponse;
import com.hbtheme.infigestback.mapper.StateRegisteredNurseMapper;
import com.hbtheme.infigestback.service.StateRegisteredNurseService;
import com.hbtheme.infigestback.tools.Constant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(Constant.NURSE_RESOURCE_API)
@RequiredArgsConstructor
@Slf4j
public class StateRegisteredNurseController extends BaseController {

	private final StateRegisteredNurseService stateRegisteredNurseService;

	private final StateRegisteredNurseMapper stateRegisteredNurseMapper;

	@PostMapping
	ResponseEntity<StateRegisteredNurseResponse> createStateRegisteredNurse(
			@RequestBody StateRegisteredNurseRequest stateRegisteredNurseRequest) {

		try {
			stateRegisteredNurseService.saveStateRegisteredNurse(stateRegisteredNurseRequest, false);
			String successMessage = String.format("Nurse \"%s %S\" created successfully!",
					stateRegisteredNurseRequest.getFirstName(), stateRegisteredNurseRequest.getLastName());
			log.info(successMessage);
			return new ResponseEntity<>(new StateRegisteredNurseResponse(successMessage), HttpStatus.CREATED);
		} catch (Exception e) {
			String errorMessage = String.format("Error while processing Nurse creation (%s)", e.getMessage());
			log.error(errorMessage);
			return new ResponseEntity<>(new StateRegisteredNurseResponse(errorMessage), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping
	ResponseEntity<StateRegisteredNurseResponse> findAllStateRegisteredNurses() {
		try {
			List<StateRegisteredNurseSimpleResponse> simpleStateRegisteredNurses = stateRegisteredNurseService
					.findAllStateRegisteredNurses().stream()
					.map(stateRegisteredNurseMapper::toSimpleResponse)
					.toList();

			return new ResponseEntity<>(StateRegisteredNurseResponse.builder()
					.message(String.format("Successfully returned %s Nurse(s)", simpleStateRegisteredNurses.size()))
					.stateRegisteredNurses(simpleStateRegisteredNurses).build(), HttpStatus.OK);
		} catch (Exception e) {
			String errorMessage = "Failed to complete get all Nurses";
			return new ResponseEntity<>(new StateRegisteredNurseResponse(errorMessage),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@GetMapping(path = "/byId/{id}")
	public ResponseEntity<StateRegisteredNurseResponse> stateRegisteredNursesById(@PathVariable(value = "id") Long id) {
		try {
			List<StateRegisteredNurseSimpleResponse> simpleStateRegisteredNurses = stateRegisteredNurseService
					.findStateRegisteredNurseById(id).stream()
					.map(stateRegisteredNurseMapper::toSimpleResponse)
					.toList();

			return new ResponseEntity<>(StateRegisteredNurseResponse.builder().message("Successfully returned 1 Nurse")
					.stateRegisteredNurses(simpleStateRegisteredNurses).build(), HttpStatus.OK);
		} catch (Exception e) {
			String errorMessage = String.format("Failed to complete get nurse (%s)", e.getMessage());
			log.error(errorMessage);
			return new ResponseEntity<>(new StateRegisteredNurseResponse(errorMessage),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping(path = "/update")
	public ResponseEntity<StateRegisteredNurseResponse> updateStateRegisteredNurseById(
			@RequestBody StateRegisteredNurseRequest stateRegisteredNurseRequest) {
		try {
			stateRegisteredNurseService.saveStateRegisteredNurse(stateRegisteredNurseRequest, true);
			String successMessage = String.format("\"Nurse with id %s updated successfully!\"!",
					stateRegisteredNurseRequest.getId());
			log.info(successMessage);
			return new ResponseEntity<>(new StateRegisteredNurseResponse(successMessage), HttpStatus.OK);
		} catch (Exception e) {
			String errorMessage = String.format("Error while processing nurse updating (%s)", e.getMessage());
			log.error(errorMessage);
			return new ResponseEntity<>(new StateRegisteredNurseResponse(errorMessage), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping(path = "/delete/{id}")
	public ResponseEntity<StateRegisteredNurseResponse> deleteStateRegisteredNurseById(@PathVariable(value = "id") Long id) {
		try {
			stateRegisteredNurseService.deleteStateRegisteredNurseById(id);
			String successMessage = String.format("\"Nurse with id %s deleted successfully!\"!", id);
			log.info(successMessage);
			return new ResponseEntity<>(new StateRegisteredNurseResponse(successMessage), HttpStatus.OK);
		} catch (DataIntegrityViolationException e) {
			String errorMessage = String.format("You cannot delete this nurse as she has already been assigned (%s)", e.getMessage());
			log.error(errorMessage);
			return new ResponseEntity<>(new StateRegisteredNurseResponse(errorMessage), HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			String errorMessage = String.format("Error while processing nurse deleting (%s)", e.getMessage());
			log.error(errorMessage);
			return new ResponseEntity<>(new StateRegisteredNurseResponse(errorMessage), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
