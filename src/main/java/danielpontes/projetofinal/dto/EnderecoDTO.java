package danielpontes.projetofinal.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record EnderecoDTO(
    @NotBlank String logradouro,
    @NotBlank String numero,
    String bairro,
    @NotBlank String cidade,
    @NotBlank String estado,
    @NotBlank @Pattern(regexp = "\\d{8}") String cep
) {}
