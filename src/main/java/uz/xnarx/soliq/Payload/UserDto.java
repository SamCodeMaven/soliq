package uz.xnarx.soliq.Payload;

import lombok.Data;

import java.util.List;

@Data
public class UserDto {
    private Long id;

    private String name;

    private String email;

    private List<CardDto> cards;
}
