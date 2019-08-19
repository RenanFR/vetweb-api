package com.vetweb.api.persist.auth;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vetweb.api.model.Species;

@Repository
public interface SpeciesRepository extends JpaRepository<Species, Long> {

}
