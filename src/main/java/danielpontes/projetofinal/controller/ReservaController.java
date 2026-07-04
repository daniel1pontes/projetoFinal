package danielpontes.projetofinal.controller;

import danielpontes.projetofinal.dto.ReservaRequestDTO;
import danielpontes.projetofinal.dto.ReservaResponseDTO;
import danielpontes.projetofinal.dto.ResumoReservaDTO;
import danielpontes.projetofinal.model.Usuario;
import danielpontes.projetofinal.service.ReservaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reservas")
public class ReservaController {

    @Autowired
    private ReservaService reservaService;

    @PostMapping
    public ResponseEntity<ReservaResponseDTO> reservar(@RequestBody @Valid ReservaRequestDTO dados, @AuthenticationPrincipal Usuario usuario) {
        var response = reservaService.reservar(dados, usuario);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/minhas")
    public ResponseEntity<List<ReservaResponseDTO>> minhasReservas(@AuthenticationPrincipal Usuario usuario) {
        return ResponseEntity.ok(reservaService.listarMinhasReservas(usuario));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelar(@PathVariable Long id, @AuthenticationPrincipal Usuario usuario) {
        reservaService.cancelarReserva(id, usuario);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/resumo")
    public ResponseEntity<ResumoReservaDTO> obterResumo() {
        var resumo = reservaService.obterResumo();
        return ResponseEntity.ok(resumo);
    }
}