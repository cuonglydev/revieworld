package com.revieworld.service.impl;

import com.revieworld.model.Popup;
import com.revieworld.repository.PopupRepository;
import com.revieworld.service.PopupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.time.LocalDateTime;

@Service
public class PopupServiceImpl implements PopupService {

    @Autowired
    private PopupRepository popupRepository;

    @Override
    public Popup getPopupById(Long id) {
        return popupRepository.findById(id).orElse(null);
    }

    @Override
    public List<Popup> getAllPopups() {
        return popupRepository.findAll();
    }

    @Override
    public List<Popup> getActivePopups() {
        return popupRepository.findByActiveTrue();
    }

    @Override
    public Popup savePopup(Popup popup) {
        // Nếu là popup mới (chưa có ID)
        if (popup.getId() == null) {
            popup.setCreatedAt(LocalDateTime.now());
            // Đảm bảo trạng thái active mặc định
            if (popup.getIsActive() == null) {
                popup.setIsActive(true);
            }
            // Đặt các giá trị mặc định nếu chưa có
            if (popup.getDuration() == null) {
                popup.setDuration(5); // 5 giây mặc định
            }
            if (popup.getPosition() == null) {
                popup.setPosition("top-right"); // Vị trí mặc định
            }
            if (popup.getAnimation() == null) {
                popup.setAnimation("fade"); // Hiệu ứng mặc định
            }
            if (popup.getPriority() == null) {
                popup.setPriority(0); // Độ ưu tiên mặc định
            }
        }
        
        // Cập nhật thời gian sửa đổi
        popup.setUpdatedAt(LocalDateTime.now());
        
        // Đồng bộ trạng thái active và isActive
        popup.setIsActive(popup.isActive());
        
        return popupRepository.save(popup);
    }

    @Override
    public void deletePopup(Long id) {
        popupRepository.deleteById(id);
    }
} 