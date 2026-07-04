package danielpontes.projetofinal.dto;

import danielpontes.projetofinal.model.UserRole;

public record UsuarioPerfilDTO(
        Long id,
        String nome,
        String email,
        String telefone,
        UserRole role,
        EnderecoDTO endereco
) {}