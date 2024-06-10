package uz.xnarx.soliq.Controller;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.xnarx.soliq.Entity.ApiResponse;
import uz.xnarx.soliq.Payload.AddCardDto;
import uz.xnarx.soliq.Payload.CardDto;
import uz.xnarx.soliq.Payload.P2PDto;
import uz.xnarx.soliq.Repository.SoliqServiceRepository;
import uz.xnarx.soliq.Service.CardService;
import uz.xnarx.soliq.Util.ApplicationConstants;

@RestController
@RequestMapping(value = "/api/card")
public class CardController {

    @Autowired
    private CardService cardService;

    @PostMapping("/addCard")
    public HttpEntity<?> addCard(@RequestBody AddCardDto cardDto) {
        ApiResponse apiResponse = cardService.addCardsToUser(cardDto);
        return ResponseEntity
                .status(apiResponse.isSuccess() ? apiResponse.getMessage().equals("Saved") ? 201 : 202 : 409)
                .body(apiResponse);
    }

    @PostMapping("/transfer")
    public HttpEntity<?> transferMoney(@RequestBody P2PDto dto) {
        ApiResponse response = cardService.transferMoney(dto);
        return ResponseEntity.status(response.isSuccess() ? 200 : 409).body(response);
    }

    @GetMapping("/getAll")
    public HttpEntity<?> getAllCards(@RequestParam(value = "page",
            defaultValue = ApplicationConstants.DEFAULT_PAGE_NUMBER) Integer page,
                                     @RequestParam(value = "size",
                                             defaultValue = ApplicationConstants.DEFAULT_PAGE_SIZE) Integer size) {

        return ResponseEntity.ok(cardService.getAllCard(page,size));
    }

    @GetMapping("/getByNumber/{number}")
    public HttpEntity<?> getById(@PathVariable("number") String cardNumber) {
        ApiResponse response = cardService.getByNumber(cardNumber);
        return ResponseEntity.status(response.isSuccess() ? 200 : 409).body(response);
    }

    @DeleteMapping("delete/{numbet}")
    public HttpEntity<?> deleteByNumber(@PathVariable("numbet") String cardNumber) {
        ApiResponse response=cardService.deleteByNumber(cardNumber);
        return ResponseEntity.status(response.isSuccess() ? 200 : 409).body(response);
    }

}
