package danielpontes.projetofinal.dto;

import danielpontes.projetofinal.model.UserRole;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UsuarioDTO(
    @NotBlank String nome,
    @NotBlank @Email String email,
    @NotBlank String senha,
    String telefone,
    @NotNull UserRole role,
    @Valid @NotNull EnderecoDTO endereco
) {}
