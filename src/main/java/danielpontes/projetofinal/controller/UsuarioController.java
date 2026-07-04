package danielpontes.projetofinal.controller;

import danielpontes.projetofinal.dto.EnderecoDTO;
import danielpontes.projetofinal.dto.UsuarioPerfilDTO;
import danielpontes.projetofinal.model.Usuario;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @GetMapping("/me")
    public ResponseEntity<UsuarioPerfilDTO> meuPerfil(@AuthenticationPrincipal Usuario usuario) {
        EnderecoDTO enderecoDTO = null;
        var endereco = usuario.getEndereco();
        if (endereco != null) {
            enderecoDTO = new EnderecoDTO(
                    endereco.getLogradouro(),
                    endereco.getNumero(),
                    endereco.getBairro(),
                    endereco.getCidade(),
                    endereco.getEstado(),
                    endereco.getCep()
            );
        }

        var perfil = new UsuarioPerfilDTO(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getTelefone(),
                usuario.getRole(),
                enderecoDTO
        );

        return ResponseEntity.ok(perfil);
    }
}