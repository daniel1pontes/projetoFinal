package danielpontes.projetofinal.dto;

import java.math.BigDecimal;
import java.util.Map;

public record ResumoReservaDTO(
    long totalReservas,
    Map<String, Long> reservasPorTipo,
    double duracaoMediaHoras,
    BigDecimal receitaTotal
) {}
