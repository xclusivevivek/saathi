package com.vvsoft.saathi.info.home;

import com.vvsoft.saathi.info.record.crud.InfoRecordCrudService;
import com.vvsoft.saathi.info.record.model.InfoRecord;
import com.vvsoft.saathi.info.record.presentation.InfoRecordDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/site")
public class HomeController {

    @Autowired
    public InfoRecordCrudService infoRecordCrudService;

    @GetMapping
    public String homePage() {
        return "home.html";
    }

    @ModelAttribute("allRecords")
    public List<InfoRecordDto> listOfRecords() {
        List<InfoRecord> records = infoRecordCrudService.getAll();
        return records.stream().map(InfoRecordDto::from).toList();
    }
}
