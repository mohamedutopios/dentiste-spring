package com.cgi.dentistapp.repository;

import com.cgi.dentistapp.entity.DentistVisitEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DentistVisitRepository extends JpaRepository<DentistVisitEntity, Long> {


    @Query
    List<DentistVisitEntity> findByOrderByDateAsc ();

    @Query
    List<DentistVisitEntity> findByDentistName(String dentistName);


}
