package danielpontes.projetofinal.dto;

import danielpontes.projetofinal.model.TipoEspaco;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record EspacoDTO(
    @NotBlank String nome,
    @NotNull TipoEspaco tipo,
    @NotNull BigDecimal precoHora,
    @NotNull Long filialId
) {}
