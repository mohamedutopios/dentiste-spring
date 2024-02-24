package com.cgi.dentistapp.controller;

import com.cgi.dentistapp.dto.DentistVisitDTO;
import com.cgi.dentistapp.dto.Response;
import com.cgi.dentistapp.entity.DentistVisitEntity;
import com.cgi.dentistapp.service.DentistVisitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;



@Controller
public class DentistAppController  {

    @Autowired
    private DentistVisitService dentistVisitService;


    @GetMapping("/")
    public String showRegisterForm(@ModelAttribute("dentistVisit") DentistVisitDTO dentistVisit) {
        return "form";
    }


    @PostMapping("/")
    public String postRegisterForm( @Valid @ModelAttribute("dentistVisit") DentistVisitDTO dentistVisit
            ,BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            return "form";
        }

        if(dentistVisitService.addVisit(dentistVisit).equals("Success")){

            model.addAttribute("result","Success");

            return "form";
        }

        model.addAttribute("result","Fail");

        return "form";
    }


    @GetMapping("/records")
    public String getResults (Model model, @ModelAttribute("dentistVisit") DentistVisitDTO dentistVisit ) {

        List<DentistVisitEntity> records = dentistVisitService.getRecordsSortedByDate();

        model.addAttribute("records",records);

        return "records";
    }

    @GetMapping("/record/{id}")
    public String getRecord (@PathVariable Long id, Model model) {

        DentistVisitEntity entity = dentistVisitService.getRecordById(id);

        model.addAttribute("id", id);
        model.addAttribute("record",entity);

        return "record";
    }


    @DeleteMapping("/records")
    @ResponseBody
    public Response deleteRecord (@RequestParam Long id) {

        return dentistVisitService.deleteRecord(id);

    }

    @PutMapping("/records")
    @ResponseBody
    public Response updateRecord (@RequestBody DentistVisitDTO dentistVisit) {


        return dentistVisitService.updateRecord(dentistVisit);
    }

    @GetMapping("/records/search")
    @ResponseBody
    public ResponseEntity<List<DentistVisitEntity>> searchRecords (@RequestParam String word) {

        List<DentistVisitEntity> resultList = dentistVisitService.getSearchResults(word);

        return ResponseEntity.ok().body(resultList);
    }



}
