import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Instagram_Repository extends JpaRepository<InstagramPhoto, Long> {
}
