package danielpontes.projetofinal.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "espacos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Espaco {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Enumerated(EnumType.STRING)
    private TipoEspaco tipo;

    @Column(nullable = false)
    private BigDecimal precoHora;

    @ManyToOne
    @JoinColumn(name = "filial_id")
    private Filial filial;
}
