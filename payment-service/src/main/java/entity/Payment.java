package entity;


import jakarta.persistence.*;
import lombok.*;


import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;

    @Column(name = "order_id", unique = true, nullable = false)
    private UUID order_id;

    @Column(name = "is_cash", length = 32)
    private Boolean is_cash;

    @Column(name = "sum", unique = true, nullable = false)
    private Double sum;

    @Column(name = "payment_date", unique = true, nullable = false)
    private LocalDateTime payment_date;

    @Column(name = "status", unique = true, nullable = false)
    private String status;

}