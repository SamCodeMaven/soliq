package uz.xnarx.soliq.Payload;

import lombok.Data;

import java.util.List;

@Data
public class AddCardDto {

    private Long userId;
    private List<CardDto> cards;
}
