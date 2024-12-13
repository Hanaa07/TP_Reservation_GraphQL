package com.hotel.tp_reservation_graphql.repositories;

import com.hotel.tp_reservation_graphql.entities.Chambre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChambreRepository extends JpaRepository<Chambre, Long> {
}
