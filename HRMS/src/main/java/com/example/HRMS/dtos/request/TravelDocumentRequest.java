package com.example.HRMS.dtos.request;

import com.example.HRMS.enums.DocumentTypes;
import com.example.HRMS.enums.OwnerType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TravelDocumentRequest {
    @NotNull(message = "Travel ID is required")
    private Long travelId;

    @NotNull(message = "Uploaded By ID is required")
    private Long uploadedById;

    @NotBlank(message = "File name is required")
    @Size(min = 3, max = 100, message = "File name must be between 3 and 100 characters")
    private String fileName;

    @NotNull(message = "Document type is required")
    private DocumentTypes documentType;

    @NotNull(message = "Owner type is required")
    private OwnerType ownerType;
}
