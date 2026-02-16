package com.example.HRMS.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.HRMS.dtos.request.TravelDocumentRequest;
import com.example.HRMS.dtos.response.TravelDocumentResponse;
import com.example.HRMS.entities.Employee;
import com.example.HRMS.entities.Travel;
import com.example.HRMS.entities.TravelDocument;
import com.example.HRMS.enums.Statuses;
import com.example.HRMS.mappers.TravelDocumentMapper;
import com.example.HRMS.repos.EmployeeRepository;
import com.example.HRMS.repos.TravelDocumentRepository;
import com.example.HRMS.repos.TravelRepository;
import com.example.HRMS.securityClasses.CustomEmployee;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TravelDocumentService {
    private final TravelDocumentRepository travelDocumentRepository;
    private final EmployeeRepository employeeRepository;
    private final TravelRepository travelRepository;
    private  final Cloudinary cloudinary;

    public TravelDocumentService(TravelDocumentRepository travelDocumentRepository,Cloudinary cloudinary, EmployeeRepository employeeRepository, TravelRepository travelRepository) {
        this.travelDocumentRepository = travelDocumentRepository;
        this.employeeRepository = employeeRepository;
        this.travelRepository = travelRepository;
        this.cloudinary = cloudinary;
    }

    @Transactional
    public TravelDocumentResponse createTravelDocument(TravelDocumentRequest request, MultipartFile file,String email, Long id){

        TravelDocument travelDocument = TravelDocumentMapper.toEntity(request);

       Employee employee = employeeRepository.findByEmail(email)
                .orElseThrow(
                        () -> new EntityNotFoundException("Employee not found with email: " + email)
                );


        String role = employee.getRole().getRoleName().toString();


       if(!"HR".equalsIgnoreCase(role)){
           boolean travelExists = employee.getTravels()
                   .stream()
                   .anyMatch(tr -> tr.getTravelId().equals(id));

           if(!travelExists){
               throw new IllegalArgumentException("You are not assigned to this travel");
           }
       }
       travelDocument.setUploadedBy(employee);

      Travel travel =  travelRepository.findById(id)
                .orElseThrow(
                        () -> new EntityNotFoundException("Travel not found with ID: " + id)
                );

      if(travel.getStatus().getStatusName() != Statuses.Approved){
          throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Travel is not approved by you.");
      }
        travelDocument.setTravel(travel);

        File convFile = new File(System.getProperty("java.io.tmpdir") + "/" + file.getOriginalFilename()+"_"+travelDocument.getTravelDocumentId());
        try {

            FileOutputStream fos = new FileOutputStream(convFile);
            fos.write(file.getBytes());
            fos.close();

            var doc = cloudinary.uploader().upload(convFile, ObjectUtils.asMap("folder", "/travelDocuments/"));

            travelDocument.setStorageUrl(doc.get("url").toString());

        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Failed to upload the file.");
        }
        travelDocument = travelDocumentRepository.save(travelDocument);

        TravelDocumentResponse travelDocumentResponse = TravelDocumentMapper.toDto(travelDocument);

        return travelDocumentResponse;
    }


    public List<TravelDocumentResponse> getAllTravelDocuments(Long travelId) {
        List<TravelDocument> travelDocuments = travelDocumentRepository.findAllByTravelId(travelId);

        List<TravelDocument> filteredTravelDocuments = travelDocuments.stream()
                .filter(tr -> !tr.isDeleted())
                .collect(Collectors.toList());

        List<TravelDocumentResponse> travelDocumentResponseList = filteredTravelDocuments
                .stream()
                .map(tr -> TravelDocumentMapper.toDto(tr))
                .collect(Collectors.toList());

        return travelDocumentResponseList;
    }

    public TravelDocumentResponse getTravelDocumentById(Long travelDocumentId, Long docId) {
        TravelDocument travelDocument = travelDocumentRepository.findByTravelIdAndDocumentId(travelDocumentId,docId);
        if (travelDocument.isDeleted())
            throw new EntityNotFoundException("Travel Document you are looking for is deleted");
        else
            return TravelDocumentMapper.toDto(travelDocument);
    }
}
