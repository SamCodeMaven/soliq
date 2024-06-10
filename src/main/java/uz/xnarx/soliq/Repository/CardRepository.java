package uz.xnarx.soliq.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.xnarx.soliq.Entity.Card;

import java.util.Optional;

public interface CardRepository extends JpaRepository<Card, Long> {
    @Query("SELECT c FROM Card c WHERE c.cardNumber = :cardNumber")
    Card findByCardNumber(String cardNumber);

}

