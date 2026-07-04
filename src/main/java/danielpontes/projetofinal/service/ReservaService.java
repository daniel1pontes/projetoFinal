package danielpontes.projetofinal.service;

import danielpontes.projetofinal.dto.ReservaRequestDTO;
import danielpontes.projetofinal.dto.ReservaResponseDTO;
import danielpontes.projetofinal.dto.ResumoReservaDTO;
import danielpontes.projetofinal.exception.ValidacaoException;
import danielpontes.projetofinal.model.Reserva;
import danielpontes.projetofinal.model.Usuario;
import danielpontes.projetofinal.repository.EspacoRepository;
import danielpontes.projetofinal.repository.ReservaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReservaService {

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private EspacoRepository espacoRepository;

    public ReservaResponseDTO reservar(ReservaRequestDTO dados, Usuario usuario) {
        var espaco = espacoRepository.findById(dados.espacoId())
                .orElseThrow(() -> new EntityNotFoundException("Espaço não encontrado"));

        if (dados.fim().isBefore(dados.inicio())) {
            throw new ValidacaoException("A data de fim deve ser após a data de início");
        }

        var conflitos = reservaRepository.findOverlappingReservations(dados.espacoId(), dados.inicio(), dados.fim());
        if (!conflitos.isEmpty()) {
            throw new ValidacaoException("O espaço já possui uma reserva para o horário selecionado");
        }

        long horas = Duration.between(dados.inicio(), dados.fim()).toHours();
        if (horas <= 0) horas = 1; // Mínimo 1 hora

        BigDecimal valorTotal = espaco.getPrecoHora().multiply(new BigDecimal(horas));

        Reserva reserva = new Reserva();
        reserva.setUsuario(usuario);
        reserva.setEspaco(espaco);
        reserva.setDataHoraInicio(dados.inicio());
        reserva.setDataHoraFim(dados.fim());
        reserva.setValorTotal(valorTotal);

        reservaRepository.save(reserva);

        return new ReservaResponseDTO(
                reserva.getId(),
                usuario.getNome(),
                espaco.getNome(),
                reserva.getDataHoraInicio(),
                reserva.getDataHoraFim(),
                reserva.getValorTotal()
        );
    }

    public ResumoReservaDTO obterResumo() {
        List<Reserva> reservas = reservaRepository.findAll();

        long totalReservas = reservas.size();
        
        Map<String, Long> reservasPorTipo = reservas.stream()
                .collect(Collectors.groupingBy(r -> r.getEspaco().getTipo().name(), Collectors.counting()));

        double duracaoMedia = reservas.stream()
                .mapToLong(r -> Duration.between(r.getDataHoraInicio(), r.getDataHoraFim()).toHours())
                .average()
                .orElse(0.0);

        BigDecimal receitaTotal = reservas.stream()
                .map(Reserva::getValorTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new ResumoReservaDTO(totalReservas, reservasPorTipo, duracaoMedia, receitaTotal);
    }
}
