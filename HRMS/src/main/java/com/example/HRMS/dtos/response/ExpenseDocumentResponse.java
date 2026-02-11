package com.example.HRMS.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExpenseDocumentResponse {
    private Long expenseId;
    private String storageUrl;

}
