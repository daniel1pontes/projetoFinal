package danielpontes.projetofinal.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record ReservaRequestDTO(
    @NotNull Long espacoId,
    @NotNull @Future LocalDateTime inicio,
    @NotNull @Future LocalDateTime fim
) {}
