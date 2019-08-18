package com.vetweb.api.model;

import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "tbl_petshop")
@Data
@EqualsAndHashCode(callSuper=false)
public class PetShop extends ClinicService {

}
