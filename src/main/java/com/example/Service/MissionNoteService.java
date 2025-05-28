package com.example.Service;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.Entity.Mission;
import com.example.Entity.MissionNote;
import com.example.Repository.MissionNoteRepository;

@Service
public class MissionNoteService {

	@Autowired
	MissionNoteRepository missionNoteRepository;

	private final UploadService uploadService;

	public MissionNoteService(UploadService uploadService) {
		this.uploadService = uploadService;
	}

	@Value("${static-folder}")
	private String staticFolder;

	public MissionNote findById(int id) {
		return missionNoteRepository.findById(id).orElse(null);
	}

	public List<MissionNote> findAllByMissionId(int missionId) {
		return missionNoteRepository.findAllByMissionId(missionId);
	}

	public void save(MissionNote missionNote) {
		missionNoteRepository.save(missionNote);
	}

	public void deleteById(int id) {
		missionNoteRepository.deleteById(id);
	}

	public void createNewMissionNote(String note, MultipartFile file, String status, Mission mission, String sender) {
		MissionNote missionNote = new MissionNote();
		missionNote.setType(status);
		missionNote.setSender(sender);
		
		if ((note != null && !note.isEmpty()) || (file != null && !file.isEmpty())) {
			
			missionNote.setNote(note);
			try {
				if (file != null && !file.isEmpty()) {
					String fileName = uploadService.saveFile(file, "images");
					missionNote.setPhoto(staticFolder + "images/" + fileName);
				} else {
					missionNote.setPhoto(null);
				}
			} catch (IOException e) {
				throw new RuntimeException("File upload failed", e);
			}
			
		}
		missionNote.setCreatedAt(new Date());
		missionNote.setMission(mission);
		missionNoteRepository.save(missionNote);
	}
}
