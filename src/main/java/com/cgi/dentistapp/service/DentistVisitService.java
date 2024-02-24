package com.cgi.dentistapp.service;

import com.cgi.dentistapp.dto.DentistVisitDTO;
import com.cgi.dentistapp.dto.Response;
import com.cgi.dentistapp.entity.DentistVisitEntity;
import com.cgi.dentistapp.repository.DentistVisitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@Transactional
public class DentistVisitService {

    @Autowired
    private DentistVisitRepository repository;

    public String addVisit(DentistVisitDTO visit) {

        LocalDate requestedDate = visit.getDate();
        LocalTime requestedTime = visit.getTime();
        String dentist = visit.getDentistName();

        boolean slotIsFree = checkTheSlot(requestedDate, requestedTime, dentist);

        if(slotIsFree) {
            DentistVisitEntity newVisit = new DentistVisitEntity(visit.getDentistName(),
                    visit.getDate(), visit.getTime(), visit.getPatientName(), visit.getComment());

            repository.save(newVisit);

            return "Success";
        }

        return "Fail";
    }


    public List<DentistVisitEntity> getRecordsSortedByDate () {

        return repository.findByOrderByDateAsc();
    }


    public Response deleteRecord (Long id) {

        Response response = new Response();

        Optional<DentistVisitEntity> theEntity = repository.findById(id);
        if(theEntity.isPresent()){
            repository.deleteById(id);
            response.setMessage("Success");
            return response;
        }
        response.setMessage("No record with given ID found");

        return response;
    }

    public DentistVisitEntity getRecordById (Long id) {

        DentistVisitEntity entity = new DentistVisitEntity();
        Optional<DentistVisitEntity> theEntity = repository.findById(id);
        if(theEntity.isPresent()){
            entity = theEntity.get();
        }

        return entity;
    }

    public Response updateRecord(DentistVisitDTO dentistVisit) {

        Response response = new Response();

        DentistVisitEntity entity;
        Optional<DentistVisitEntity> theEntity = repository.findById(dentistVisit.getId());
        boolean slotIsFree = checkTheSlot(dentistVisit.getDate(),dentistVisit.getTime(),dentistVisit.getDentistName());
        if(theEntity.isPresent()) {
            if ((dentistVisit.getDate().equals(theEntity.get().getDate())
                    && (dentistVisit.getTime().equals(theEntity.get().getTime())))) {
                slotIsFree = true;
            }
            if (slotIsFree) {
                entity = theEntity.get();
                entity.setDentistName(dentistVisit.getDentistName());
                entity.setDate(dentistVisit.getDate());
                entity.setTime(dentistVisit.getTime());
                entity.setPatientName(dentistVisit.getPatientName());
                entity.setComment(dentistVisit.getComment());
                repository.save(entity);
                response.setMessage("Success");
                return response;
            }
        }
        response.setMessage("Fail");
        return response;
    }

    public List<DentistVisitEntity> getSearchResults (String word) {

        List<DentistVisitEntity> allRecords = repository.findAll();

        return allRecords.stream().filter(r-> (r.getDentistName().equalsIgnoreCase(word)
        || (r.getDentistName().split(" ")[0].equalsIgnoreCase(word))
        || (r.getDentistName().split(" ")[1].equalsIgnoreCase(word)))).collect(Collectors.toList());
    }


    public boolean checkTheSlot (LocalDate requestedDate, LocalTime requestedTime, String dentistName) {

        List<DentistVisitEntity> allVisits = repository.findAll();

        return allVisits.stream().noneMatch((v ->
                ((v.getDentistName().equals(dentistName)) && ((v.getDate().equals(requestedDate) && (requestedTime.isBefore(v.getTime().plusHours(1)))))
                        && (requestedTime.plusHours(1).isAfter(v.getTime())))));

    }


}
