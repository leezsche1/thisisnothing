package inflearn.exercise.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class MemberLoginResponseDTO {

    private String accessToken;
    private String refreshToken;

    private Long id;
    private String memberEmail;
    private String name;
}
