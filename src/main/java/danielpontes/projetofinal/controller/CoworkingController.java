package danielpontes.projetofinal.controller;

import danielpontes.projetofinal.dto.EspacoDTO;
import danielpontes.projetofinal.dto.FilialDTO;
import danielpontes.projetofinal.model.Espaco;
import danielpontes.projetofinal.model.Filial;
import danielpontes.projetofinal.repository.EspacoRepository;
import danielpontes.projetofinal.repository.FilialRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
public class CoworkingController {

    @Autowired
    private FilialRepository filialRepository;

    @Autowired
    private EspacoRepository espacoRepository;

    @PostMapping("/filiais")
    @Transactional
    public ResponseEntity criarFilial(@RequestBody @Valid FilialDTO dados) {
        Filial filial = new Filial();
        filial.setNome(dados.nome());
        filial.setCnpj(dados.cnpj());
        filial.setTelefone(dados.telefone());
        filialRepository.save(filial);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/filiais")
    public ResponseEntity<List<Filial>> listarFiliais() {
        return ResponseEntity.ok(filialRepository.findAll());
    }

    @PostMapping("/espacos")
    @Transactional
    public ResponseEntity criarEspaco(@RequestBody @Valid EspacoDTO dados) {
        Filial filial = filialRepository.findById(dados.filialId())
                .orElseThrow(() -> new EntityNotFoundException("Filial não encontrada"));

        Espaco espaco = new Espaco();
        espaco.setNome(dados.nome());
        espaco.setTipo(dados.tipo());
        espaco.setPrecoHora(dados.precoHora());
        espaco.setFilial(filial);
        
        espacoRepository.save(espaco);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/espacos")
    public ResponseEntity<List<Espaco>> listarEspacos() {
        return ResponseEntity.ok(espacoRepository.findAll());
    }
}
