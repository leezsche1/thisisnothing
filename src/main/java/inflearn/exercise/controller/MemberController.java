package inflearn.exercise.controller;

import inflearn.exercise.domain.Member;
import inflearn.exercise.domain.MemberRole;
import inflearn.exercise.dto.IdCheckDTO;
import inflearn.exercise.dto.MemberJoinDTO;
import inflearn.exercise.dto.MemberLoginDTO;
import inflearn.exercise.dto.MemberLoginResponseDTO;
import inflearn.exercise.security.util.JwtTokenizer;
import inflearn.exercise.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Validated
public class MemberController {

    private final MemberService memberService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtTokenizer jwtTokenizer;
    private final RedisTemplate redisTemplate;


    private final Long refreshTime = 60 * 60 * 24 * 7L;

    @PostMapping("/join")
    public ResponseEntity join(@RequestBody @Valid MemberJoinDTO memberJoinDTO, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        Member member = new Member();
        member.setEmail(memberJoinDTO.getMemberEmail());
        member.setName(memberJoinDTO.getMemberNm());
        member.setMemberPwd(bCryptPasswordEncoder.encode(memberJoinDTO.getMemberPwd()));
        member.setBirth(memberJoinDTO.getBirth());
        member.setMemberPhone(memberJoinDTO.getMemberPhone());

        memberService.join(member, memberJoinDTO.getMemberAgree());

        return new ResponseEntity(HttpStatus.CREATED);
    }

    @PostMapping("/join/idCheck")
    public ResponseEntity idCheck(@RequestBody IdCheckDTO idCheckDTO) {

        boolean aboolean = memberService.existsByEmail(idCheckDTO.getEmail());
        if (aboolean == true) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity(HttpStatus.OK);
        }

    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid MemberLoginDTO memberLoginDTO, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        Optional<Member> byEmail = memberService.findByEmail(memberLoginDTO.getMemberEmail());
        if (byEmail.isEmpty()) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        Member member = byEmail.get();
        if (!bCryptPasswordEncoder.matches(memberLoginDTO.getMemberPwd(), member.getMemberPwd())) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }

        List<String> roles = member.getRoles().stream().map(MemberRole::getRoleName).collect(Collectors.toList());

        String accessToken = jwtTokenizer.createAccessToken(member.getId(), member.getEmail(), member.getName(), roles);
        String refreshToken = jwtTokenizer.createRefreshToken(member.getId(), member.getEmail(), member.getName(), roles);

        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(member.getEmail(), refreshToken, refreshTime);

        MemberLoginResponseDTO memberLoginResponseDTO = new MemberLoginResponseDTO();
        memberLoginResponseDTO.setAccessToken(accessToken);
        memberLoginResponseDTO.setRefreshToken(refreshToken);
        memberLoginResponseDTO.setMemberEmail(member.getEmail());
        memberLoginResponseDTO.setId(member.getId());
        memberLoginResponseDTO.setName(member.getName());


        return new ResponseEntity(memberLoginResponseDTO, HttpStatus.OK);

    }

//    @PostMapping("/auth/sms")
//
//    @PostMapping("/auth/smsCheck")
}
