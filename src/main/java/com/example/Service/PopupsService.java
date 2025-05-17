package com.example.Service;

import com.example.Entity.Popups;
import com.example.Repository.PopupsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class PopupsService {
    @Autowired
    private PopupsRepository popupsRepository;

    public Popups createSinglePopup(Popups popup) {
        popupsRepository.deleteAll(); // Xóa hết popup cũ
        return popupsRepository.save(popup); // Lưu popup mới
    }

    public Optional<Popups> getLatestActivePopup() {
        List<Popups> list = popupsRepository.findByStatusTrue();
        return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
    }
} 