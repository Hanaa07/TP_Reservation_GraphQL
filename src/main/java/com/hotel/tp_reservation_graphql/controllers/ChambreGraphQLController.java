package com.hotel.tp_reservation_graphql.controllers;

import com.hotel.tp_reservation_graphql.entities.Chambre;
import com.hotel.tp_reservation_graphql.repositories.ChambreRepository;
import com.hotel.tp_reservation_graphql.repositories.ReservationRepository;
import com.hotel.tp_reservation_graphql.requests.ChambreInput;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class ChambreGraphQLController {

    private final ChambreRepository chambreRepository;

    public ChambreGraphQLController(
            ChambreRepository chambreRepository
    ) {
        this.chambreRepository = chambreRepository;
    }

    @QueryMapping
    public List<Chambre> getAllChambres() {
        return chambreRepository.findAll();
    }

    @QueryMapping
    public Chambre getChambreById(@Argument Long id) {
        return chambreRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Chambre not found"));
    }


    @MutationMapping
    public Chambre createChambre(@Argument ChambreInput input) {
        Chambre chambre = new Chambre();
        chambre.setTypeChambre(input.getTypeChambre());
        chambre.setPrix(input.getPrix());
        chambre.setDisponible(input.isDisponible());
        return chambreRepository.save(chambre);
    }

    @MutationMapping
    public Chambre updateChambre(@Argument Long id, @Argument ChambreInput input) {
        Chambre chambre = chambreRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Chambre not found"));

        chambre.setTypeChambre(input.getTypeChambre());
        chambre.setPrix(input.getPrix());
        chambre.setDisponible(input.isDisponible());
        return chambreRepository.save(chambre);
    }

    @MutationMapping
    public boolean deleteChambre(@Argument Long id) {
        if (!chambreRepository.existsById(id)) {
            throw new RuntimeException("Chambre not found");
        }
        chambreRepository.deleteById(id);
        return true;
    }
}