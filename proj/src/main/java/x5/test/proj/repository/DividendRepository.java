package x5.test.proj.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import x5.test.proj.domain.Dividend;

@Repository
public interface DividendRepository extends JpaRepository<Dividend, Long> {
}