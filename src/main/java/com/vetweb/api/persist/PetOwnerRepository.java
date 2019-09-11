package com.vetweb.api.persist;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vetweb.api.model.PetOwner;

@Repository
public interface PetOwnerRepository extends JpaRepository<PetOwner, String> {

}
