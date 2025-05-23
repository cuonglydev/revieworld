package com.example.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Entity.MissionNote;
import com.example.Repository.MissionNoteRepository;

@Service
public class MissionNoteService {

	@Autowired
	MissionNoteRepository missionNoteRepository;
	
	public MissionNote findById(int id) {
		return missionNoteRepository.findById(id).orElse(null);
	}
	
	public List<MissionNote> findAllByMissionId(int missionId){
		return missionNoteRepository.findAllByMissionId(missionId);
	}
	
	public void save(MissionNote missionNote) {
		missionNoteRepository.save(missionNote);
	}
	
	public void deleteById(int id) {
		missionNoteRepository.deleteById(id);
	}
}
