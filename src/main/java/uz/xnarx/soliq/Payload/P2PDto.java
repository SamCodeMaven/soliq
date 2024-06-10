package uz.xnarx.soliq.Payload;

import lombok.Data;

@Data
public class P2PDto {
    private String fromCardNumber;
    private String toCardNumber;
    private double amount;
}
