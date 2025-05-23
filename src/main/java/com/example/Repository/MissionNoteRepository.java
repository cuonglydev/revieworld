package com.example.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Entity.MissionNote;

@Repository
public interface MissionNoteRepository extends JpaRepository<MissionNote, Integer> {
	List<MissionNote> findAllByMissionId(int missionId);
}
