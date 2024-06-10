package uz.xnarx.soliq.Service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import uz.xnarx.soliq.Entity.ApiResponse;
import uz.xnarx.soliq.Entity.Card;
import uz.xnarx.soliq.Entity.User;
import uz.xnarx.soliq.Payload.CardDto;
import uz.xnarx.soliq.Payload.UserDto;
import uz.xnarx.soliq.Repository.UserRepository;
import uz.xnarx.soliq.Util.CommonUtills;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CardService cardService;


    @Transactional
    public ApiResponse getAllUsers(Integer page, Integer size) {
        Pageable pageable = CommonUtills.simplePageable(page, size);
        Page<User> users = userRepository.findAll(pageable);
        return new ApiResponse("Success",
                true,
                users.getTotalElements(),
                users.getTotalPages(),
                users.getContent().stream().map(this::convertToDto).collect(Collectors.toList()));
    }

    @Transactional
    public ApiResponse saveUser(UserDto userDto) {
        try {
            User user;
            if (userDto.getId() != null) {
                user = userRepository.findById(userDto.getId())
                        .orElseThrow(() -> new IllegalStateException("User with this ID not found"));
            } else {
                user = new User();
            }
            user.setName(userDto.getName());
            user.setEmail(userDto.getEmail());

            if (userDto.getCards() != null) {
                List<Card> cards = cardService.convertToCardEntities(userDto.getCards(), user);

                user.setCards(cards);
            }
            userRepository.save(user);
            return new ApiResponse(userDto.getId() != null ? "Edited" : "Saved", true, convertToDto(user));
        } catch (Exception e) {
            return new ApiResponse(e.getMessage(), false);
        }
    }
    private UserDto convertToDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        if (user.getCards() != null) {
            List<CardDto> cardDtos = user.getCards().stream().map(card -> {
                CardDto cardDto = new CardDto();
                cardDto.setCardNumber(card.getCardNumber());
                cardDto.setBalance(card.getBalance());
                cardDto.setSecurityCode(card.getSecurityCode());
                return cardDto;
            }).collect(Collectors.toList());
            userDto.setCards(cardDtos);
        }
        return userDto;
    }

    @Transactional
    public ApiResponse getById(Long aLong) {
        try {
            User user=userRepository.findById(aLong).orElseThrow(() -> new IllegalStateException("User with this ID not found"));
            return new ApiResponse("User found", true, convertToDto(user));
        }catch (Exception e){
            return new ApiResponse("User not found",false);
        }
    }

    @Transactional
    public ApiResponse removeById(Long aLong) {
        try {
            User user = userRepository.findById(aLong).orElseThrow(() -> new IllegalStateException("User with this ID not found"));
            userRepository.delete(user);
            return new ApiResponse("User deleted", true, convertToDto(user));
        }catch (Exception e){
            return new ApiResponse("User not found",false);
        }
    }
}
