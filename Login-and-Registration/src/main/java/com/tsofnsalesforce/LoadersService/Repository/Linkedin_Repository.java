import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Linkedin_Repository extends JpaRepository<LinkedinPost, Long> {
}