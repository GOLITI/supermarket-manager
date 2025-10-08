package com.supermarket.manager.repository;

import com.supermarket.manager.model.fournisseur.Fournisseur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FournisseurRepository extends JpaRepository<Fournisseur, Long> {

    Optional<Fournisseur> findByCode(String code);

    List<Fournisseur> findByActifTrue();

    List<Fournisseur> findByNomContainingIgnoreCase(String nom);
}

