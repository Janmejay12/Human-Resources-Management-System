package com.example.HRMS.mappers;

import com.example.HRMS.dtos.request.ReferalRequest;
import com.example.HRMS.dtos.request.TravelDocumentRequest;
import com.example.HRMS.dtos.response.ReferalResponse;
import com.example.HRMS.dtos.response.TravelDocumentResponse;
import com.example.HRMS.entities.Referal;
import com.example.HRMS.entities.TravelDocument;
import org.springframework.stereotype.Component;

@Component
public class ReferalMapper {
    public static Referal toEntity(ReferalRequest request) {
        if (request == null) {
            return null;
        }
        Referal referal = new Referal();
        referal.setCandidateEmail(request.getCandidateEmail());
        referal.setCandidateName(request.getCandidateName());
        referal.setShortNote(request.getShortNote());
        return  referal;
    }

    public static ReferalResponse toDto(Referal referal) {
        if (referal == null) {
            return null;
        }

        ReferalResponse referalResponse = new ReferalResponse();
        referalResponse.setReferalId(referal.getReferalId());
        referalResponse.setReferalGivenBy(referal.getReferalGivenBy().getEmployeeId());
        referalResponse.setShortNote(referal.getShortNote());
        referalResponse.setCandidateName(referal.getCandidateName());
        referalResponse.setCandidateEmail(referal.getCandidateEmail());
        referalResponse.setCvURLPath(referal.getCvURLPath());
        return referalResponse;
    }
}
