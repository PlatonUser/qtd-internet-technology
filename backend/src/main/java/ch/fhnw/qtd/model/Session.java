package ch.fhnw.qtd.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "session")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(nullable = false)
    @Builder.Default
    private LocalDateTime startedAt = LocalDateTime.now();

    @Builder.Default
    private boolean completed = false;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "session_players", joinColumns = @JoinColumn(name = "session_id"))
    @Column(name = "player_name")
    @OrderColumn(name = "player_index")
    @Builder.Default
    private List<String> players = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "session_question_ids", joinColumns = @JoinColumn(name = "session_id"))
    @Column(name = "question_id")
    @OrderColumn(name = "question_index")
    @Builder.Default
    private List<Long> questionIds = new ArrayList<>();

    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @JsonIgnore
    private List<SessionAnswer> answers = new ArrayList<>();
}