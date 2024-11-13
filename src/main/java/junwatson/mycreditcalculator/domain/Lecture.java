package junwatson.mycreditcalculator.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.GenerationType.*;

@Entity
@NoArgsConstructor
@Getter
public class Lecture {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @JoinColumn(nullable = false)
    @ManyToOne
    private Member member;

    @Column(nullable = false)
    private String name;

    private Double credit;

    private String major;

    @Column(nullable = false)
    private Integer semester;

    @Builder
    private Lecture(Member member, String name, Double credit, String major, Integer semester) {
        this.member = member;
        member.getLectures()
                .add(this);
        this.name = name;
        this.credit = credit;
        this.major = major;
        this.semester = semester;
    }
}
