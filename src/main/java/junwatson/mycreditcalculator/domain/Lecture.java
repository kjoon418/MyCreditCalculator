package junwatson.mycreditcalculator.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.*;
import static jakarta.persistence.GenerationType.*;

@Entity
@NoArgsConstructor
@Getter
public class Lecture {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @JoinColumn(nullable = false)
    @ManyToOne(fetch = LAZY)
    private Member member;

    @Column(nullable = false)
    private String name;

    private Double credit;

    private String major;

    @Column(nullable = false)
    private Integer semester;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LectureType type;

    @Builder
    private Lecture(Member member, String name, Double credit, String major, Integer semester, LectureType type) {
        this.member = member;
        member.getLectures().add(this);
        this.name = name;
        this.credit = credit;
        this.major = major;
        this.semester = semester;
        this.type = type;
    }

    public Lecture update(String name, Double credit, String major, Integer semester, LectureType type) {
        this.name = name;
        this.credit = credit;
        this.major = major;
        this.semester = semester;
        this.type = type;

        return this;
    }
}
