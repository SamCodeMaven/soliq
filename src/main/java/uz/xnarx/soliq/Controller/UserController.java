package uz.xnarx.soliq.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.xnarx.soliq.Entity.ApiResponse;
import uz.xnarx.soliq.Payload.UserDto;
import uz.xnarx.soliq.Service.UserService;
import uz.xnarx.soliq.Util.ApplicationConstants;

import java.util.List;

@RestController
@RequestMapping(value = "/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/save")
    public HttpEntity<?> saveUser(@RequestBody UserDto userDto) {
        ApiResponse apiResponse = userService.saveUser(userDto);
        return ResponseEntity
                .status(apiResponse.isSuccess() ? apiResponse.getMessage().equals("Saved") ? 201 : 202 : 409)
                .body(apiResponse);
    }

    @GetMapping("/getAll")
    public HttpEntity<?> gatAllUsers(@RequestParam(value = "page",
            defaultValue = ApplicationConstants.DEFAULT_PAGE_NUMBER) Integer page,
                                     @RequestParam(value = "size",
                                             defaultValue = ApplicationConstants.DEFAULT_PAGE_SIZE) Integer size) {
        return ResponseEntity.ok(userService.getAllUsers(page, size));
    }

    @GetMapping("/getById/{id}")
    public HttpEntity<?> getUserById(@PathVariable(value = "id")Long aLong){
        ApiResponse response=userService.getById(aLong);
        return ResponseEntity.status(response.isSuccess()?200:409).body(response);
    }

    @DeleteMapping("/delete/{id}")
    public HttpEntity<?> deleteUser(@PathVariable(value = "id")Long aLong){
        ApiResponse response=userService.removeById(aLong);
        return ResponseEntity.status(response.isSuccess()?200:409).body(response);
    }


}
