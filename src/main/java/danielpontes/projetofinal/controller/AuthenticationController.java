package danielpontes.projetofinal.controller;

import danielpontes.projetofinal.dto.LoginDTO;
import danielpontes.projetofinal.dto.LoginResponseDTO;
import danielpontes.projetofinal.dto.UsuarioDTO;
import danielpontes.projetofinal.model.Endereco;
import danielpontes.projetofinal.model.Usuario;
import danielpontes.projetofinal.repository.UsuarioRepository;
import danielpontes.projetofinal.security.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid LoginDTO data) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.email(), data.senha());
        var auth = this.authenticationManager.authenticate(usernamePassword);

        var token = tokenService.generateToken((Usuario) auth.getPrincipal());

        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    @PostMapping("/registrar")
    public ResponseEntity registrar(@RequestBody @Valid UsuarioDTO data) {
        if (this.repository.findByEmail(data.email()) != null) return ResponseEntity.badRequest().build();

        String encryptedPassword = new BCryptPasswordEncoder().encode(data.senha());
        Usuario newUser = new Usuario();
        newUser.setNome(data.nome());
        newUser.setEmail(data.email());
        newUser.setSenha(encryptedPassword);
        newUser.setTelefone(data.telefone());
        newUser.setRole(data.role());

        Endereco endereco = new Endereco();
        endereco.setLogradouro(data.endereco().logradouro());
        endereco.setNumero(data.endereco().numero());
        endereco.setBairro(data.endereco().bairro());
        endereco.setCidade(data.endereco().cidade());
        endereco.setEstado(data.endereco().estado());
        endereco.setCep(data.endereco().cep());
        endereco.setUsuario(newUser);

        newUser.setEndereco(endereco);

        this.repository.save(newUser);

        return ResponseEntity.ok().build();
    }
}
