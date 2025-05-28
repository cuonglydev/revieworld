package com.example.Service;

import java.util.List;
import com.example.Entity.Mission;
import com.example.Entity.Order;
import com.example.Entity.OrderContent;
import com.example.Entity.OrderPhoto;
import com.example.Entity.User;
import com.example.Repository.MissionRepository;
import com.example.Repository.OrderRepository;
import com.example.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Date;


@Service
public class MissionService {

    @Autowired
    private MissionRepository missionRepository;

    
    public List<Mission> findAllByUserId(int userId) {
        return missionRepository.findAllByUserId(userId);
    }
    
    public void save(Mission mission) {
        missionRepository.save(mission);
    }

    public void delete(int id) {
        missionRepository.deleteById(id);
    }
    
    public List<Mission> findAllByOrderId(int orderId){
    	return missionRepository.findAllByOrderId(orderId);
    }

    public List<Mission> findAllByOrderIdAndStatus(int orderId, String status){ 
    	return missionRepository.findAllByOrderIdAndStatus(orderId, status);
    }
    
    public Mission findById(int id) {
    	return missionRepository.findById(id).orElse(null);
    }
    
    public Mission findByOrderIdAndUserId(int orderId, int userId) { 
    	return missionRepository.findByOrderIdAndUserId(orderId, userId);
    }
    
    
    public List<Mission> findAllByOrderIdAndStatusIn(int orderId, List<String> statuses){
    	return missionRepository.findAllByOrderIdAndStatusIn(orderId, statuses);
    }

    @Autowired
    private UserRepository userRepository;
    
    @Autowired 
    private OrderService orderService;
   
    @Autowired
    private UserService userService;
    
    @Autowired
    private MissionNoteService missionNoteService;
    
    @Autowired
    private OrderPhotoService orderPhotoService;
    
    @Autowired
    private OrderContentService orderContentService;
    
    public void handleMissionStatusChange(Mission mission, Order order, User missionUser, String status, String note, MultipartFile file, String sender, RedirectAttributes redirectAttributes) {
    	switch (status) {
		case "SUCCESS" :
			  mission.setStatus(status);
	            mission.setStatusDate(new Date());
	            missionNoteService.createNewMissionNote(note, file, status, mission, sender);
	            missionRepository.save(mission);

	            int quantityDone = order.getQuantityDone() + 1;
	            order.setQuantityDone(quantityDone);
	            if (quantityDone == order.getQuantity()) {
	                order.setStatus("CLOSED");
	            }
	            orderService.save(order);

	            missionUser.setBonusAmount(missionUser.getBonusAmount() + mission.getAmount());
	            userService.save(missionUser);

	            redirectAttributes.addFlashAttribute("success", "Xác nhận thành công!");
	            break;

		
		 case "REQUEST_EDIT":
	        case "REQUEST_REFUSE":
	        
	            mission.setStatus(status);
	            mission.setStatusDate(new Date());
	            missionNoteService.createNewMissionNote(note, file, status, mission, sender);
	            missionRepository.save(mission);
	            redirectAttributes.addFlashAttribute("success", "Yêu cầu xử lý thành công!");
	            break;
	            
	       
	            
	        case "REFUSED":
	        	  	mission.setStatus(status);
		            mission.setStatusDate(new Date());
		            missionNoteService.createNewMissionNote(note, file, status, mission, sender);
		            missionRepository.save(mission);
		            
		            
		            int quantityWorked = order.getWorkedQuantity() - 1;
		            order.setWorkedQuantity(quantityWorked);
		            orderService.save(order);
		            
		            if(mission.getOrderPhoto() != null) {
		            	  OrderPhoto orderPhoto = orderPhotoService.findById(mission.getOrderPhoto().getId());
		            	  orderPhoto.setStatus("ACTIVE");
		            	  orderPhotoService.save(orderPhoto);
		            }
		            
		            if(mission.getOrderContent() != null) {
		            	OrderContent orderContent = orderContentService.findById(mission.getOrderContent().getId());
		            	orderContent.setStatus("ACTIVE");
		            	orderContentService.save(orderContent);
		            }
		            redirectAttributes.addFlashAttribute("success", "Yêu cầu xử lý thành công!");
		            
		            break;
		            
	        default:
	            redirectAttributes.addFlashAttribute("danger", "Trạng thái không hợp lệ!");
		          
		}
    }
//
//    // Bắt đầu nhiệm vụ (Trạng thái: WAITING)
//    public Mission startMission(int missionId) {
//        Mission mission = missionRepository.findById(missionId)
//                .orElseThrow(() -> new IllegalArgumentException("Invalid mission Id:" + missionId));
//
//        // Chuyển trạng thái sang WAITING
//        mission.setStatus("WAITING");
//        mission.setStatusDate(new Date());
//        missionRepository.save(mission);
//
//        return mission;
//    }

//    // Duyệt nhiệm vụ (Trạng thái: APPROVED)
//    public Mission approveMission(int missionId) {
//        Mission mission = missionRepository.findById(missionId)
//                .orElseThrow(() -> new IllegalArgumentException("Invalid mission Id:" + missionId));
//
//        // Cập nhật trạng thái và cộng tiền cho người thực hiện nhiệm vụ
//        mission.setStatus("APPROVED");
//        mission.setStatusDate(new Date());
//
//        User user = mission.getUser();
//        user.setAmount(user.getAmount() + mission.getAmount()); // Cộng tiền vào ví người thực hiện
//        userRepository.save(user);
//
//        missionRepository.save(mission);
//
//        return mission;
//    }
//
//    // Từ chối nhiệm vụ (Trạng thái: REJECTED)
//    public Mission rejectMission(int missionId) {
//        Mission mission = missionRepository.findById(missionId)
//                .orElseThrow(() -> new IllegalArgumentException("Invalid mission Id:" + missionId));
//
//        // Cập nhật trạng thái sang REJECTED
//        mission.setStatus("REJECTED");
//        mission.setStatusDate(new Date());
//
//        // Trả lại nhiệm vụ cho người thuê và cộng số lượng đã làm vào đơn hàng
//        mission.getOrder().setQuantity(mission.getOrder().getQuantityDone());
//
//        missionRepository.save(mission);
//
//        return mission;
//    }
//
//    // Người dùng yêu cầu chỉnh sửa nhiệm vụ
//    public Mission requestEditMission(int missionId) {
//        Mission mission = missionRepository.findById(missionId)
//                .orElseThrow(() -> new IllegalArgumentException("Invalid mission Id:" + missionId));
//
//        // Chuyển trạng thái lại thành WAITING khi yêu cầu chỉnh sửa
//        mission.setStatus("WAITING");
//        mission.setStatusDate(new Date());
//
//        missionRepository.save(mission);
//
//        return mission;
//    }
//
//    // Kiểm tra nếu nhiệm vụ đã ở trạng thái "WAITING" quá lâu (3 ngày) mà không có
//    // thay đổi
//    public void autoApproveMission(int missionId) {
//        Mission mission = missionRepository.findById(missionId)
//                .orElseThrow(() -> new IllegalArgumentException("Invalid mission Id:" + missionId));
//
//        long differenceInDays = (new Date().getTime() - mission.getStatusDate().getTime()) / (1000 * 60 * 60 * 24);
//        if (differenceInDays >= 3 && mission.getStatus().equals("WAITING")) {
//            approveMission(missionId); // Tự động duyệt nếu 3 ngày trôi qua
//        }
//    }
}

