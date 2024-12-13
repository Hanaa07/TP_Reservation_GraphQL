package com.hotel.tp_reservation_graphql.controllers;

import com.hotel.tp_reservation_graphql.entities.Utilisateur;

import java.util.List;

import com.hotel.tp_reservation_graphql.repositories.UtilisateurRepository;
import com.hotel.tp_reservation_graphql.requests.UtilisateurInput;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import org.springframework.security.crypto.password.PasswordEncoder;

@Controller
public class UtilisateurGraphQLController {

    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;

    public UtilisateurGraphQLController(
            UtilisateurRepository utilisateurRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.utilisateurRepository = utilisateurRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @QueryMapping
    public List<Utilisateur> getAllUtilisateurs() {
        return utilisateurRepository.findAll();
    }

    @MutationMapping
    public Utilisateur createUtilisateur(@Argument UtilisateurInput input) {
        // Check if username already exists
        if (utilisateurRepository.findByNom(input.getNom()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setNom(input.getNom());
        // Encode password before saving
        utilisateur.setMotDePasse(passwordEncoder.encode(input.getMotDePasse()));
        utilisateur.setRole(input.getRole());
        return utilisateurRepository.save(utilisateur);
    }

    @MutationMapping
    public Utilisateur updateUtilisateur(@Argument Long id, @Argument UtilisateurInput input) {
        Utilisateur utilisateur = utilisateurRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur not found"));

        utilisateur.setNom(input.getNom());
        // Only update password if a new one is provided
        if (input.getMotDePasse() != null && !input.getMotDePasse().isEmpty()) {
            utilisateur.setMotDePasse(passwordEncoder.encode(input.getMotDePasse()));
        }
        utilisateur.setRole(input.getRole());
        return utilisateurRepository.save(utilisateur);
    }

    @MutationMapping
    public boolean deleteUtilisateur(@Argument Long id) {
        if (!utilisateurRepository.existsById(id)) {
            throw new RuntimeException("Utilisateur not found");
        }
        utilisateurRepository.deleteById(id);
        return true;
    }
}