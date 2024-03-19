package inflearn.exercise.service;

import inflearn.exercise.domain.Member;
import inflearn.exercise.domain.MemberAgree;
import inflearn.exercise.domain.MemberRole;
import inflearn.exercise.domain.Role;
import inflearn.exercise.repository.MemberAgreeRepository;
import inflearn.exercise.repository.MemberRepository;
import inflearn.exercise.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberAgreeRepository memberAgreeRepository;
    private final RoleRepository roleRepository;

    @Transactional
    public void join(Member member, int agreement) {

        Optional<Role> roleUser = roleRepository.findByRole("ROLE_USER");


        if (agreement == 1) {

            Long id1 = Long.valueOf(1);
            Optional<MemberAgree> byId = memberAgreeRepository.findById(id1);
            MemberAgree memberAgree = byId.get();
            member.setMemberAgree(memberAgree);
        } else {
            Long id2 = Long.valueOf(2);
            Optional<MemberAgree> byId = memberAgreeRepository.findById(id2);
            MemberAgree memberAgree = byId.get();
            member.setMemberAgree(memberAgree);
        }


        MemberRole memberRole = new MemberRole();
        memberRole.setRole(roleUser.get());

        member.addRole(memberRole);
        memberRepository.save(member);

    }

    public boolean existsByEmail(String email) {

        boolean aBoolean = memberRepository.existsByEmail(email);

        return aBoolean;

    }


    public Optional<Member> findByEmail(String email) {
        Optional<Member> byEmail = memberRepository.findByEmail(email);
        return byEmail;
    }


}
