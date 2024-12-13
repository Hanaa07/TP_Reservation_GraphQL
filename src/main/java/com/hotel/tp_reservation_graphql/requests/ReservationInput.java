package com.hotel.tp_reservation_graphql.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationInput {
    private Long clientId;
    private List<Long> chambresIds;
    private Date dateDebut;
    private Date dateFin;
    private String preferences;
}
