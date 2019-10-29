package com.vetweb.api.persist;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vetweb.api.model.Animal;

@Repository
public interface AnimalRepository extends JpaRepository<Animal, Long> {

}
