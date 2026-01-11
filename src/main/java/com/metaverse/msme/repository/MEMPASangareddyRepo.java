package com.metaverse.msme.repository;

import com.metaverse.msme.model.MEMPASangareddy;
import com.metaverse.msme.model.SangareddyMsmeUnitEntity;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MEMPASangareddyRepo extends JpaRepository<MEMPASangareddy, Integer> {
    @Query(value = "SELECT dh FROM MEMPASangareddy dh")
    List<MEMPASangareddy> findNextChunk(Integer lastId, PageRequest of);
}
