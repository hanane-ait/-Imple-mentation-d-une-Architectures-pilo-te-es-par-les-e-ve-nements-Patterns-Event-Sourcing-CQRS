package demoescsrsaxon.query.entities;

import demoescsrsaxon.enums.OperationType;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
@Entity
@Builder @Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class AccountOperation {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date date;
    private double ampmount;
    @Enumerated(EnumType.STRING)
    private OperationType type;
    private String currency ;
    @ManyToOne
    private Account account;
}
