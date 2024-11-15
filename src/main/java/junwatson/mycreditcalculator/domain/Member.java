package junwatson.mycreditcalculator.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.*;
import static jakarta.persistence.FetchType.*;
import static jakarta.persistence.GenerationType.*;

@Entity
@NoArgsConstructor
@Getter
public class Member {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "member", cascade = ALL, orphanRemoval = true, fetch = LAZY)
    private List<Lecture> lectures = new ArrayList<>();

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MemberRole role;

    @Builder
    private Member(String email, String password, String name, MemberRole role) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.role = role;
    }

    /**
     * 유저 엔티티를 수정하는 메서드<br>
     * 사용 전 반드시 유효한 데이터인지 검증이 필요하다
     */
    public Member update(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;

        return this;
    }
}
