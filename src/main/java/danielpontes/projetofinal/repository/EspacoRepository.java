package danielpontes.projetofinal.repository;

import danielpontes.projetofinal.model.Espaco;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EspacoRepository extends JpaRepository<Espaco, Long> {
    List<Espaco> findByFilialId(Long filialId);
}