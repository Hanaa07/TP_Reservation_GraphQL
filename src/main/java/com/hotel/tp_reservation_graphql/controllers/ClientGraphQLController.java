package com.hotel.tp_reservation_graphql.controllers;

import com.hotel.tp_reservation_graphql.entities.Reservation;
import com.hotel.tp_reservation_graphql.repositories.ClientRepository;
import com.hotel.tp_reservation_graphql.repositories.ReservationRepository;
import com.hotel.tp_reservation_graphql.entities.Client;
import com.hotel.tp_reservation_graphql.requests.ClientInput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
public class ClientGraphQLController {

    private final ClientRepository clientRepository;
    private final ReservationRepository reservationRepository;

    public ClientGraphQLController(
            ClientRepository clientRepository,
            ReservationRepository reservationRepository
    ) {
        this.clientRepository = clientRepository;
        this.reservationRepository = reservationRepository;
    }

    @QueryMapping
    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    @QueryMapping
    public Client getClientById(@Argument Long id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client not found"));
    }

    @MutationMapping
    public Client createClient(@Argument ClientInput input) {
        Client client = new Client();
        client.setNom(input.getNom());
        client.setPrenom(input.getPrenom());
        client.setEmail(input.getEmail());
        client.setTelephone(input.getTelephone());
        return clientRepository.save(client);
    }

    @MutationMapping
    public Client updateClient(@Argument Long id, @Argument ClientInput input) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client not found"));

        client.setNom(input.getNom());
        client.setPrenom(input.getPrenom());
        client.setEmail(input.getEmail());
        client.setTelephone(input.getTelephone());
        return clientRepository.save(client);
    }

    @MutationMapping
    public boolean deleteClient(@Argument Long id) {
        if (!clientRepository.existsById(id)) {
            throw new RuntimeException("Client not found");
        }
        clientRepository.deleteById(id);
        return true;
    }
}