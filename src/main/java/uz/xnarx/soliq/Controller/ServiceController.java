package uz.xnarx.soliq.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import uz.xnarx.soliq.Entity.ApiResponse;
import uz.xnarx.soliq.Payload.ServiceDto;
import uz.xnarx.soliq.Service.SoliqService;
import uz.xnarx.soliq.Util.ApplicationConstants;

@RestController
@RequestMapping(value = "/api/service")
public class ServiceController {

    @Autowired
    private SoliqService soliqService;

    @PostMapping("/save")
    public HttpEntity<?> saveService(@RequestBody ServiceDto serviceDto) {
        ApiResponse apiResponse = soliqService.saveService(serviceDto);
        return ResponseEntity
                .status(apiResponse.isSuccess() ? apiResponse.getMessage().equals("Saved") ? 201 : 202 : 409)
                .body(apiResponse);
    }
    @GetMapping("/getAll")
    public HttpEntity<?> getAllServices(@RequestParam(value = "page",
            defaultValue = ApplicationConstants.DEFAULT_PAGE_NUMBER) Integer page,
                                        @RequestParam(value = "size",
                                                defaultValue = ApplicationConstants.DEFAULT_PAGE_SIZE) Integer size) {
        return ResponseEntity.ok(soliqService.getAllServices(page,size));
    }

    @GetMapping("/getById/{id}")
    public HttpEntity<?> getServiceById(@PathVariable(value = "id") Long id)
    {
        ApiResponse response = soliqService.getByServiceId(id);
        return ResponseEntity.status(response.isSuccess() ? 200 : 409).body(response);

    }
    @DeleteMapping("/deleteById/{id}")
    public HttpEntity<?> deleteServiceById(@PathVariable(value = "id") Long id)
    {
        ApiResponse response = soliqService.removeServiceById(id);
        return ResponseEntity.status(response.isSuccess() ? 200 : 409).body(response);

    }


}
