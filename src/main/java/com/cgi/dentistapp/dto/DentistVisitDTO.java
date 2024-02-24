package com.cgi.dentistapp.dto;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Transient;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;
import java.util.TreeSet;


public class DentistVisitDTO {

    private Long id;

    @Size(min = 1, max = 40)
    private String dentistName;

    @JsonIgnore
    private Set<String> dentists = new TreeSet<>();

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    @NotNull
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime time;

    @Size(min = 1, max = 40)
    private String patientName;

    @Size(max = 40)
    private String comment;

    public DentistVisitDTO() {

        dentists.add("Marco Polo");
        dentists.add("Tristan Pastriste");
        dentists.add("Audrey Dread");
        dentists.add("Alex Flex");
        dentists.add("Seb Cbien");
    }

    public DentistVisitDTO(String dentistName, LocalDate date, LocalTime time, String patientName, String comment) {
        this.dentistName = dentistName;
        this.date = date;
        this.time = time;
        this.patientName = patientName;
        this.comment = comment;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<String> getDentists() {
        return dentists;
    }

    public void setDentists(Set<String> dentists) {
        this.dentists = dentists;
    }

    public String getDentistName() {
        return dentistName;
    }

    public void setDentistName(String dentistName) {
        this.dentistName = dentistName;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
