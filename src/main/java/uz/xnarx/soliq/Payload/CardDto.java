package uz.xnarx.soliq.Payload;

import lombok.Data;

@Data
public class CardDto {
    private String cardNumber;


    private double balance;

    private String securityCode;
}
