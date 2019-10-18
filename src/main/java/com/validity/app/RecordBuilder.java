package com.validity.app;

import org.apache.commons.csv.CSVRecord;

import java.util.ArrayList;
import java.util.List;

class RecordBuilder {
    static List<Record> build(List<CSVRecord> records) {
        List<Record> result = new ArrayList<>();
        for (int i = 1; i < records.size(); i++) {
            result.add(new Record(records.get(i)));
        }
        return result;
    }
}
