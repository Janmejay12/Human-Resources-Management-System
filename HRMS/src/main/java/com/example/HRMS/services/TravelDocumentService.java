package com.example.HRMS.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.HRMS.dtos.request.TravelDocumentRequest;
import com.example.HRMS.dtos.response.TravelDocumentResponse;
import com.example.HRMS.entities.Employee;
import com.example.HRMS.entities.Travel;
import com.example.HRMS.entities.TravelDocument;
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
    private final ModelMapper modelMapper;
    private  final Cloudinary cloudinary;

    public TravelDocumentService(TravelDocumentRepository travelDocumentRepository,Cloudinary cloudinary, EmployeeRepository employeeRepository, TravelRepository travelRepository, ModelMapper modelMapper) {
        this.travelDocumentRepository = travelDocumentRepository;
        this.employeeRepository = employeeRepository;
        this.travelRepository = travelRepository;
        this.modelMapper = modelMapper;
        this.cloudinary = cloudinary;
    }

    @Transactional
    public TravelDocumentResponse createTravelDocument(TravelDocumentRequest request, MultipartFile file,String email){

        TravelDocument travelDocument = new TravelDocument();
        travelDocument.setDocumentType(request.getDocumentType());
        travelDocument.setFileName(request.getFileName());
        travelDocument.setOwnerType(request.getOwnerType());

       Employee employee = employeeRepository.findByEmail(email)
                .orElseThrow(
                        () -> new EntityNotFoundException("Employee not found with email: " + email)
                );


        String role = employee.getRole().getRoleName().toString();


       if(!"HR".equalsIgnoreCase(role)){
           boolean travelExists = employee.getTravels()
                   .stream()
                   .anyMatch(tr -> tr.getTravelId().equals(request.getTravelId()));

           if(!travelExists){
               throw new IllegalArgumentException("You are not assigned to this travel");
           }
       }
       travelDocument.setUploadedBy(employee);

      Travel travel =  travelRepository.findById(request.getTravelId())
                .orElseThrow(
                        () -> new EntityNotFoundException("Travel not found with ID: " + request.getTravelId())
                );

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

        TravelDocumentResponse travelDocumentResponse = new TravelDocumentResponse();
//        travelDocumentResponse.setUploadedById(employee.getEmployeeId());
//        travelDocumentResponse.setTravelId(travel.getTravelId());

        travelDocument = travelDocumentRepository.save(travelDocument);
        travelDocumentResponse = modelMapper.map(travelDocument,TravelDocumentResponse.class);


        return travelDocumentResponse;
    }


    public List<TravelDocumentResponse> getAllTravelDocuments() {
        List<TravelDocument> travelDocuments = travelDocumentRepository.findAll();

        List<TravelDocument> filteredTravelDocuments = travelDocuments.stream()
                .filter(tr -> !tr.isDeleted())
                .collect(Collectors.toList());

        List<TravelDocumentResponse> travelDocumentResponseList = filteredTravelDocuments
                .stream()
                .map(tr -> modelMapper.map(tr, TravelDocumentResponse.class))
                .collect(Collectors.toList());

        return travelDocumentResponseList;
    }

    public TravelDocumentResponse getTravelDocumentById(Long travelDocumentId) {
        TravelDocument travelDocument = travelDocumentRepository.findById(travelDocumentId).orElseThrow(() -> new EntityNotFoundException("Travel Document not found with ID: " + travelDocumentId));
        if (travelDocument.isDeleted())
            throw new EntityNotFoundException("Travel Document you are looking for is deleted");
        else
            return modelMapper.map(travelDocument, TravelDocumentResponse.class);
    }
}
