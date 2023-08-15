package com.hbtheme.infigestback.service.impl;

import com.hbtheme.infigestback.dto.PatientRequest;
import com.hbtheme.infigestback.mapper.PatientMapper;
import com.hbtheme.infigestback.model.Patient;
import com.hbtheme.infigestback.model.StateRegisteredNurse;
import com.hbtheme.infigestback.repository.PatientDao;
import com.hbtheme.infigestback.service.PatientService;
import com.hbtheme.infigestback.service.StateRegisteredNurseService;
import com.hbtheme.infigestback.service.validator.PatientValidator;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {

	private final PatientDao patientDao;
	
	private final PatientValidator patientValidator;
	
	private final PatientMapper patientMapper;

	private final StateRegisteredNurseService stateRegisteredNurseService;

	@Override
	public List<Patient> findPatientById(Long patientId) {
		if (patientId == null) {
			throw new IllegalArgumentException();
		}
		List<Patient> patients = new ArrayList<>();
		Patient patient = patientDao.findById(patientId).orElseThrow(() -> new EntityNotFoundException("Patient not found"));
		patients.add(patient);
		return patients;
	}

	@Override
	public void savePatient(PatientRequest patientRequest, boolean isIdRequired) {
		if (patientRequest == null) {
			throw new IllegalArgumentException();
		}
		
		List<String> errors = patientValidator.validate(patientRequest, isIdRequired);
		if (!errors.isEmpty()) {
			throw new IllegalStateException(String.join("; ", errors));
		}

		if (isIdRequired) {
			findPatientById(patientRequest.getId());
		}

		List<StateRegisteredNurse> stateRegisteredNurses = new ArrayList<>();

		for (Long id : patientRequest.getStateRegisteredNursesIds()) {
			StateRegisteredNurse stateRegisteredNurse = stateRegisteredNurseService.findStateRegisteredNurseById(id).get(0);
			stateRegisteredNurses.add(stateRegisteredNurse);
		}
		
		Patient patient = patientMapper.toModel(patientRequest, stateRegisteredNurses);
		patientDao.save(patient);
	}

	@Override
	public List<Patient> findAllPatients() {
		return patientDao.findAll();
	}

	@Override
	public void deletePatientById(Long id) {
		if (id == null || findPatientById(id).isEmpty()) {
			throw new IllegalArgumentException();
		}
		findPatientById(id);
		patientDao.deleteById(id);
	}

}
