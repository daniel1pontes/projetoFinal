package danielpontes.projetofinal.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ReservaResponseDTO(
    Long id,
    String nomeUsuario,
    String nomeEspaco,
    LocalDateTime inicio,
    LocalDateTime fim,
    BigDecimal valorTotal
) {}
