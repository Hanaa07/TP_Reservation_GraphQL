package com.hotel.tp_reservation_graphql.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientInput {
    private String nom;
    private String prenom;
    private String email;
    private String telephone;
}