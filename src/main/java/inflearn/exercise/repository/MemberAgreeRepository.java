package inflearn.exercise.repository;

import inflearn.exercise.domain.MemberAgree;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberAgreeRepository extends JpaRepository<MemberAgree, Long> {
}
