package com.hotel.tp_reservation_graphql.controllers;

import com.hotel.tp_reservation_graphql.entities.Chambre;
import com.hotel.tp_reservation_graphql.entities.Client;
import com.hotel.tp_reservation_graphql.entities.Reservation;
import com.hotel.tp_reservation_graphql.repositories.ChambreRepository;
import com.hotel.tp_reservation_graphql.repositories.ClientRepository;
import com.hotel.tp_reservation_graphql.repositories.ReservationRepository;

import java.util.List;

import com.hotel.tp_reservation_graphql.requests.ReservationInput;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
public class ReservationGraphQLController {

    private final ReservationRepository reservationRepository;
    private final ClientRepository clientRepository;
    private final ChambreRepository chambreRepository;

    public ReservationGraphQLController(
            ReservationRepository reservationRepository,
            ClientRepository clientRepository,
            ChambreRepository chambreRepository
    ) {
        this.reservationRepository = reservationRepository;
        this.clientRepository = clientRepository;
        this.chambreRepository = chambreRepository;
    }

    @QueryMapping
    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    @QueryMapping
    public Reservation getReservationById(@Argument Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));
    }

    @MutationMapping
    public Reservation createReservation(@Argument ReservationInput input) {
        Reservation reservation = new Reservation();

        // Set client
        Client client = clientRepository.findById(input.getClientId())
                .orElseThrow(() -> new RuntimeException("Client not found"));
        reservation.setClient(client);

        // Set rooms
        List<Chambre> chambres = chambreRepository.findAllById(input.getChambresIds());
        if (chambres.size() != input.getChambresIds().size()) {
            throw new RuntimeException("One or more chambres not found");
        }

        // Check room availability
        boolean allAvailable = chambres.stream().allMatch(Chambre::isDisponible);
        if (!allAvailable) {
            throw new RuntimeException("Some rooms are not available");
        }

        // Update room availability
        chambres.forEach(chambre -> {
            chambre.setDisponible(false);
            chambre.setReservation(reservation);
        });

        reservation.setChambres(chambres);
        reservation.setDateDebut(input.getDateDebut());
        reservation.setDateFin(input.getDateFin());
        reservation.setPreferences(input.getPreferences());

        return reservationRepository.save(reservation);
    }

    @MutationMapping
    public Reservation updateReservation(@Argument Long id, @Argument ReservationInput input) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));

        // Update client
        Client client = clientRepository.findById(input.getClientId())
                .orElseThrow(() -> new RuntimeException("Client not found"));
        reservation.setClient(client);

        // Reset previous rooms
        reservation.getChambres().forEach(chambre -> {
            chambre.setDisponible(true);
            chambre.setReservation(null);
        });

        // Set new rooms
        List<Chambre> chambres = chambreRepository.findAllById(input.getChambresIds());
        if (chambres.size() != input.getChambresIds().size()) {
            throw new RuntimeException("One or more chambres not found");
        }

        // Check room availability
        boolean allAvailable = chambres.stream().allMatch(Chambre::isDisponible);
        if (!allAvailable) {
            throw new RuntimeException("Some rooms are not available");
        }

        // Update room availability
        chambres.forEach(chambre -> {
            chambre.setDisponible(false);
            chambre.setReservation(reservation);
        });

        reservation.setChambres(chambres);
        reservation.setDateDebut(input.getDateDebut());
        reservation.setDateFin(input.getDateFin());
        reservation.setPreferences(input.getPreferences());

        return reservationRepository.save(reservation);
    }

    @MutationMapping
    public boolean deleteReservation(@Argument Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));

        // Release rooms
        reservation.getChambres().forEach(chambre -> {
            chambre.setDisponible(true);
            chambre.setReservation(null);
        });

        reservationRepository.deleteById(id);
        return true;
    }
}