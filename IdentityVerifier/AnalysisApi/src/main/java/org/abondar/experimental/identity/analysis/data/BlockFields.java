package org.abondar.experimental.identity.analysis.data;

public enum BlockFields {
    Nationality("Nationality"),
    DateOfBirth("Date.+birth"),
    PlaceOfBirth("Place.+birth"),
    DateOfExpiration("Date.+expiration"),
    DateOfIssue("Date.+issue"),
    GivenNames("Given.+Names"),
    Surname("Surname");


    private String val;

    public String getVal() {
        return val;
    }

    BlockFields(String val) {
        this.val = val;
    }
}
