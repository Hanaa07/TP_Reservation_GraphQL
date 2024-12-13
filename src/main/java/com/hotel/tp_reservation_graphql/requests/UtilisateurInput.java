package com.hotel.tp_reservation_graphql.requests;

import com.hotel.tp_reservation_graphql.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UtilisateurInput {
    private String nom;
    private String motDePasse;
    private Role role;
}
