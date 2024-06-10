package uz.xnarx.soliq.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Card {

    @Id
    private String cardNumber;


    private double balance;

    private String securityCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    private User user;

}
