package uz.xnarx.soliq.Payload;

import lombok.Data;

@Data
public class ServiceDto {

    private Long id;
    private String name;
    private double transactionFee;

}
