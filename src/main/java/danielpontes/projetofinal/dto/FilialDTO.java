package danielpontes.projetofinal.dto;

import jakarta.validation.constraints.NotBlank;

public record FilialDTO(
    @NotBlank String nome,
    @NotBlank String cnpj,
    String telefone
) {}
