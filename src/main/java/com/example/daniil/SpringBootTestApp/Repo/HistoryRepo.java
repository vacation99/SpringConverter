package com.example.daniil.SpringBootTestApp.Repo;

import com.example.daniil.SpringBootTestApp.Models.History;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface HistoryRepo extends CrudRepository<History, Long> {
    List<History> findByCharCode1(String charCode1);
    List<History> findByCharCode2(String charCode2);
    List<History> findByDate(String date);
}
