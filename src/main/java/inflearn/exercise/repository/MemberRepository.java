package inflearn.exercise.repository;

import inflearn.exercise.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    boolean existsByEmail(String Email);

    Optional<Member> findByEmail(String email);
}
