package uz.xnarx.soliq.Service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import uz.xnarx.soliq.Entity.ApiResponse;
import uz.xnarx.soliq.Entity.Card;
import uz.xnarx.soliq.Entity.Services;
import uz.xnarx.soliq.Entity.User;
import uz.xnarx.soliq.Exception.InvalidCardBalanceException;
import uz.xnarx.soliq.Payload.AddCardDto;
import uz.xnarx.soliq.Payload.CardDto;
import uz.xnarx.soliq.Payload.P2PDto;
import uz.xnarx.soliq.Repository.CardRepository;
import uz.xnarx.soliq.Repository.SoliqServiceRepository;
import uz.xnarx.soliq.Repository.UserRepository;
import uz.xnarx.soliq.Util.CommonUtills;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class CardService {

    private static final int CARD_NUMBER_LENGTH = 16;

    public static String generateCardNumber() {
        Random random = new Random();
        StringBuilder cardNumber = new StringBuilder();
        for (int i = 0; i < CARD_NUMBER_LENGTH; i++) {
            cardNumber.append(random.nextInt(10));
        }
        return cardNumber.toString();
    }

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public ApiResponse addCardsToUser(AddCardDto addCardsRequest) {
        try {
            User user = userRepository.findById(addCardsRequest.getUserId())
                    .orElseThrow(() -> new IllegalStateException("User with this ID not found"));

            List<Card> cards = convertToCardEntities(addCardsRequest.getCards(), user);


            cardRepository.saveAll(cards);

            return new ApiResponse("Cards added", true);
        } catch (Exception e) {
            return new ApiResponse("Error", false);
        }
    }
    protected List<Card> convertToCardEntities(List<CardDto> cardDtos, User user) {
        return cardDtos.stream().map(dto -> {
            if (dto.getBalance() < 0) {
                throw new InvalidCardBalanceException("Card balance cannot be negative: " + dto.getCardNumber());
            }
            Card card = new Card();
            card.setCardNumber(dto.getCardNumber() != null ? dto.getCardNumber() : generateCardNumber());
            card.setBalance(dto.getBalance());
            card.setSecurityCode(dto.getSecurityCode());
            card.setUser(user);
            return card;
        }).collect(Collectors.toList());
    }


    @Transactional
    public ApiResponse getAllCard(Integer page, Integer size) {
        Pageable pageable= CommonUtills.simplePageable(page,size);
        Page<Card> cards = cardRepository.findAll(pageable);
        return new ApiResponse("Success",
                true,
                cards.getTotalElements(),
                cards.getTotalPages(),
                cards.getContent().stream().map(this::convertToCardDto).collect(Collectors.toList()));
    }
    private CardDto convertToCardDto(Card card) {
        CardDto cardDto = new CardDto();
        cardDto.setCardNumber(card.getCardNumber());
        cardDto.setBalance(card.getBalance());
        cardDto.setSecurityCode(card.getSecurityCode());
        return cardDto;
    }

    @Transactional
    public ApiResponse getByNumber(String cardNumber) {
        try {
            Card card = cardRepository.findByCardNumber(cardNumber);
            return new ApiResponse("Card found", true, convertToCardDto(card));
        } catch (Exception e) {
            return new ApiResponse("Card not found", false);
        }
    }

    @Transactional
    public ApiResponse deleteByNumber(String cardNumber) {

        try {
            Card card = cardRepository.findByCardNumber(cardNumber);
            cardRepository.delete(card);
            return new ApiResponse("Card deleted", true, convertToCardDto(card));
        }catch (Exception e) {
            return new ApiResponse("Card not found", false);
        }
    }
    @Autowired
    private SoliqServiceRepository soliqServiceRepository;

    @Transactional
    public ApiResponse transferMoney(P2PDto dto) {
        try {
            // Retrieve the "p2p" service
            Services p2pService = soliqServiceRepository.findById(3L)
                    .orElseThrow(() -> new IllegalStateException("Service 'p2p' not found"));

            // Retrieve the cards
            Card fromCard = cardRepository.findByCardNumber(dto.getFromCardNumber());
            if (fromCard.getCardNumber()==null){
                throw  new IllegalStateException("Sender card not found");
            }
            Card toCard = cardRepository.findByCardNumber(dto.getToCardNumber());
            if (toCard.getCardNumber()==null){
                throw  new IllegalStateException("Receiver card not found");
            }

            // Calculate the transaction fee and the total amount to deduct
            double fee = (dto.getAmount()*p2pService.getTransactionFee())/100;
            double totalAmountToDeduct = dto.getAmount() + fee;

            // Check if the fromCard has enough balance
            if (fromCard.getBalance() < totalAmountToDeduct) {
                return new ApiResponse("Insufficient balance", false);
            }

            // Perform the transfer
            fromCard.setBalance(fromCard.getBalance() - totalAmountToDeduct);
            toCard.setBalance(toCard.getBalance() + dto.getAmount());

            // Save the updated cards
            cardRepository.save(fromCard);
            cardRepository.save(toCard);

            return new ApiResponse("Transfer successful", true,totalAmountToDeduct);
        } catch (Exception e) {
            return new ApiResponse("Error during transfer: " + e.getMessage(), false);
        }
    }
}
