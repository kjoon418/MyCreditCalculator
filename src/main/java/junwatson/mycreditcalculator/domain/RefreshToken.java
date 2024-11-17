package junwatson.mycreditcalculator.domain;

import jakarta.persistence.*;
import lombok.*;

import static jakarta.persistence.GenerationType.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Setter
    private String token;

    @Builder
    public RefreshToken(Member member, String token) {
        this.member = member;
        if (member != null) {
            member.setRefreshToken(this);
        }
        this.token = token;
    }
}
