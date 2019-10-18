package com.validity.app;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletContext;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

@RequestMapping("")
@Controller
public class HomeController {
    @Autowired
    ServletContext context;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView home() {
        FileModel file = new FileModel();
        return new ModelAndView("home", "command", file);
    }

    @RequestMapping(value="/", method = RequestMethod.POST)
    public String fileUpload(@Validated FileModel file, BindingResult result, ModelMap model) throws IOException {
        if (result.hasErrors()) {
            return "home";
        } else {
            MultipartFile multipartFile = file.getFile();
            String content = new String(multipartFile.getBytes());
            Reader in = new StringReader(content);
            CSVParser parser = new CSVParser(in, CSVFormat.DEFAULT);
            List<CSVRecord> list = parser.getRecords();
            List<Record> records = RecordBuilder.build(list);
            Set<Record> checked = new HashSet<>();
            List<List<String>> potentialDuplicates = new ArrayList<>();
            List<String> nonDuplicates = new ArrayList<>();
            for (Record record1: records) {
                if (checked.contains(record1)) {
                    continue;
                }
                List<String> potentialDuplicateSet = new ArrayList<>();
                List<String> duplicateSet = new ArrayList<>();
                for (Record record2: records) {
                    if (record1.isDuplicate(record2)) {
                        potentialDuplicateSet.add(record2.toString());
                        checked.add(record2);
                    }
                }
                if (potentialDuplicateSet.size() > 1) {
                    potentialDuplicates.add(potentialDuplicateSet);
                } else {
                    nonDuplicates.add(record1.toString());
                }
            }
            model.addAttribute("potentialDuplicates", potentialDuplicates);
            model.addAttribute("nonDuplicates", nonDuplicates);
            return "success";
        }
    }
}
