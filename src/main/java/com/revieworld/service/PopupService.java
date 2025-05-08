package com.revieworld.service;

import com.revieworld.model.Popup;
import java.util.List;

public interface PopupService {
    Popup getPopupById(Long id);
    List<Popup> getAllPopups();
    List<Popup> getActivePopups();
    Popup savePopup(Popup popup);
    void deletePopup(Long id);
} 