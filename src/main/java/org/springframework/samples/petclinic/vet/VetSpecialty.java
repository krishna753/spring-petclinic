//package org.springframework.samples.petclinic.vet;
//
//import javax.persistence.*;
//import java.io.Serializable;
//
//@Entity
//@Table(name="vet_specialties")
//@NamedQuery(name="VetSpecialty.findAll", query="SELECT s FROM VetSpecialty s")
//public class VetSpecialty implements Serializable {
//	private static final long serialVersionUID = 1L;
//
//	@EmbeddedId
//	private VetSpecialtyPK id;
//
//	@ManyToOne
//	@JoinColumn(name="vet_id")
//	private Vet vet;
//
//	@ManyToOne
//	@JoinColumn(name="specialty_id")
//	private Specialty specialty;
//
//	public Vet getVet() {
//		return vet;
//	}
//
//	public void setVet(Vet vet) {
//		this.vet = vet;
//	}
//
//	public Specialty getSpecialty() {
//		return specialty;
//	}
//
//	public void setSpecialty(Specialty specialty) {
//		this.specialty = specialty;
//	}
//
//}
