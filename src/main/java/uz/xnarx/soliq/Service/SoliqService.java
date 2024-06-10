package uz.xnarx.soliq.Service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import uz.xnarx.soliq.Entity.ApiResponse;
import uz.xnarx.soliq.Entity.Card;
import uz.xnarx.soliq.Entity.Services;
import uz.xnarx.soliq.Payload.ServiceDto;
import uz.xnarx.soliq.Repository.SoliqServiceRepository;
import uz.xnarx.soliq.Util.CommonUtills;

import java.util.stream.Collectors;

@Service
public class SoliqService {

    @Autowired
    private SoliqServiceRepository serviceRepository;


    @Transactional
    public ApiResponse saveService(ServiceDto serviceDto) {
        try {
            Services service;
            if (serviceDto.getId() != null) {
                service = serviceRepository.findById(serviceDto.getId()).orElseThrow(() -> new IllegalStateException("Service not found"));
            }else {
            service=new Services();
            }
            service.setName(serviceDto.getName());
            service.setTransactionFee(serviceDto.getTransactionFee());
            serviceRepository.save(service);
            return new ApiResponse(serviceDto.getId()!=null?"Edited":"Saved", true, service);
        } catch (Exception e) {
            return new ApiResponse("Error creating service", false);
        }
    }

    @Transactional
    public ApiResponse getAllServices(Integer page, Integer size) {
        Pageable pageable= CommonUtills.simplePageable(page,size);
        Page<Services> services = serviceRepository.findAll(pageable);
        return new ApiResponse("Success",
                true,
                services.getTotalElements(),
                services.getTotalPages(),
                services.getContent().stream().map(this::convertToServicesDto).collect(Collectors.toList()));
    }

    protected Services convertToServicesDto(Services services) {
        ServiceDto serviceDto = new ServiceDto();
        serviceDto.setId(services.getId());
        serviceDto.setName(services.getName());
        serviceDto.setTransactionFee(services.getTransactionFee());
        return services;
    }

    @Transactional
    public ApiResponse getByServiceId(Long id) {

        try {
            Services service = serviceRepository.findById(id).orElseThrow(() -> new IllegalStateException("Service not found"));
            return new ApiResponse("Success", true, convertToServicesDto(service));
        }catch (Exception e) {
            return new ApiResponse("Error getting service", false);
        }
    }

    @Transactional
    public ApiResponse removeServiceById(Long id) {
        try {
            Services service = serviceRepository.findById(id).orElseThrow(() -> new IllegalStateException("Service not found"));
            serviceRepository.delete(service);
            return new ApiResponse("Service Deleted", true, convertToServicesDto(service));
        }catch (Exception e) {
            return new ApiResponse("Error removing service", false);
        }
    }
}
