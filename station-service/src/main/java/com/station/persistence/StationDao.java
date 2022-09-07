package com.station.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.station.bean.Station;

@Repository
public interface StationDao extends JpaRepository<Station, Integer>{

}
