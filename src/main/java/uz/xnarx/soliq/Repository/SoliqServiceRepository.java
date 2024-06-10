package uz.xnarx.soliq.Repository;


import org.springframework.data.jpa.repository.JpaRepository;
import uz.xnarx.soliq.Entity.Services;

public interface SoliqServiceRepository extends JpaRepository<Services, Long> {
}
