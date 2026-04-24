package ch.fhnw.qtd.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "session_answer")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class SessionAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", nullable = false)
    private Session session;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    @Column(length = 1000)
    private String answerText;
}