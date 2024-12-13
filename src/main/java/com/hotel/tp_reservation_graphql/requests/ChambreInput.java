package com.hotel.tp_reservation_graphql.requests;

import com.hotel.tp_reservation_graphql.enums.TypeChambre;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChambreInput {
    private TypeChambre typeChambre;
    private Double prix;
    private boolean disponible;
}
