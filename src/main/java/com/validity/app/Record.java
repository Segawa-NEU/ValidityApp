package com.validity.app;
import org.apache.commons.codec.language.Metaphone;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class Record {
    private long recordId;
    private String original;
    private String id;
    private String firstName;
    private String lastName;
    private String company;
    private String email;
    private String address1;
    private String address2;
    private String zip;
    private String city;
    private String state_long;
    private String state;
    private String phone;
    private Metaphone metaphone;
    Record(CSVRecord record) {
        this.recordId = record.getRecordNumber();
        this.original = String.join(",", record);
        this.id = record.get(0);
        this.firstName = record.get(1);
        this.lastName = record.get(2);
        this.company = record.get(3);
        this.email = record.get(4);
        this.address1 = record.get(5);
        this.address2 = record.get(6);
        this.zip = record.get(7);
        this.city = record.get(8);
        this.state_long = record.get(9);
        this.state = record.get(10);
        this.phone = record.get(11);
        this.metaphone = new Metaphone();
    }

    private boolean isNameMatch(Record record) {
        int score = 0;
        if (!firstName.isEmpty() && !record.firstName.isEmpty()) {
            score += StringUtils.getLevenshteinDistance(this.firstName, record.firstName);
        }
        if (!metaphone.isMetaphoneEqual(firstName, record.firstName)) {
            score += 2;
        }
        if (!lastName.isEmpty() && !record.lastName.isEmpty()) {
            score += StringUtils.getLevenshteinDistance(this.lastName, record.lastName);
        }
        if (!metaphone.isMetaphoneEqual(lastName, record.lastName)) {
            score += 2;
        }
        return score < 8;
    }

    private boolean isCompanyMatch(Record record) {
        int score = 0;
        if (!company.isEmpty() && !record.company.isEmpty()) {
            score += StringUtils.getLevenshteinDistance(this.company, record.company);
        }
        if (!metaphone.isMetaphoneEqual(company, record.company)) {
            score += 2;
        }
        return score < 4;
    }

    private boolean isEmailMatch(Record record) {
        int score = 0;
        if (!email.isEmpty() && !record.email.isEmpty()) {
            score += StringUtils.getLevenshteinDistance(this.email, record.email);
        }
        return score < 3;
    }

    private boolean isAddressMatch(Record record) {
        int score = 0;
        if (!address1.isEmpty() && !record.address1.isEmpty()) {
            score += StringUtils.getLevenshteinDistance(this.address1, record.address1);
        }
        if (!metaphone.isMetaphoneEqual(address1, record.address1)) {
            score += 2;
        }
        if (!address2.isEmpty() && !record.address2.isEmpty()) {
            score += StringUtils.getLevenshteinDistance(this.address2, record.address2);
        }
        if (!metaphone.isMetaphoneEqual(address2, record.address2)) {
            score += 2;
        }
        if (!zip.isEmpty() && !record.zip.isEmpty()) {
            score += StringUtils.getLevenshteinDistance(this.zip, record.zip);
        }
        if (!metaphone.isMetaphoneEqual(zip, record.zip)) {
            score += 2;
        }
        if (!city.isEmpty() && !record.city.isEmpty()) {
            score += StringUtils.getLevenshteinDistance(this.city, record.city);
        }
        if (!metaphone.isMetaphoneEqual(city, record.city)) {
            score += 2;
        }
        if (!state_long.isEmpty() && !record.state_long.isEmpty()) {
            score += StringUtils.getLevenshteinDistance(this.state_long, record.state_long);
        }
        if (!metaphone.isMetaphoneEqual(state_long, record.state_long)) {
            score += 2;
        }
        if (!state.isEmpty() && !record.state.isEmpty()) {
            score += StringUtils.getLevenshteinDistance(this.state, record.state);
        }
        if (!metaphone.isMetaphoneEqual(state, record.state)) {
            score += 2;
        }
        return score < 14;
    }

    private boolean isPhoneMatch(Record record) {
        return this.phone.equals(record.phone);
    }

    boolean isDuplicate(Record record) {
        int score = 0;
        if (!this.isNameMatch(record)) {
            score += 8;
        }
        if (!this.isCompanyMatch(record)) {
            score += 2;
        }
        if (!this.isEmailMatch(record)) {
            score += 2;
        }
        if (!this.isAddressMatch(record)) {
            score += 1;
        }
        if (!this.isPhoneMatch(record)) {
            score += 1;
        }
        return score < 8;
    }

    boolean sameId(Record record) {
        return this.id.equals(record.id);
    }

    @Override
    public String toString() {
        return original;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Record) {
            Record record = (Record)obj;
            return this.original.equals(record.original) && this.recordId == record.recordId;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.original.hashCode()*(int)this.recordId;
    }
}
