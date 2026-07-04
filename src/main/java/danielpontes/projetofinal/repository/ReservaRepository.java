package danielpontes.projetofinal.repository;

import danielpontes.projetofinal.model.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    
    @Query("SELECT r FROM Reserva r WHERE r.espaco.id = :espacoId " +
           "AND (:inicio < r.dataHoraFim AND :fim > r.dataHoraInicio)")
    List<Reserva> findOverlappingReservations(@Param("espacoId") Long espacoId, 
                                              @Param("inicio") LocalDateTime inicio, 
                                              @Param("fim") LocalDateTime fim);
}
