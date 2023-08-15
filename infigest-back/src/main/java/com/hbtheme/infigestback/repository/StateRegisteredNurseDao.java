package com.hbtheme.infigestback.repository;

import com.hbtheme.infigestback.model.Patient;
import com.hbtheme.infigestback.model.StateRegisteredNurse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StateRegisteredNurseDao extends JpaRepository<StateRegisteredNurse, Long> {
}
