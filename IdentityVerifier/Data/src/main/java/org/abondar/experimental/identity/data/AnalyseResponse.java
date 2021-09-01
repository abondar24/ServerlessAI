package org.abondar.experimental.identity.data;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class AnalyseResponse {

    private String nationality;
    private String dateOfBirth;
    private String placeOfBirth;
    private String expirationDate;
    private String issueDate;
    private String givenNames;
    private String surname;
    private String documentNumber;
}
