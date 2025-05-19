package com.example.Controller.Admin;

import java.lang.ProcessBuilder.Redirect;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.Entity.Language;
import com.example.Entity.Order;
import com.example.Entity.OrderType;
import com.example.Repository.OrderRepository;
import com.example.Service.LanguageService;
import com.example.Service.OrderTypeService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
public class LanguageMangeController {

    @Autowired
    private LanguageService languageService; 

    @Autowired
    private OrderTypeService orderTypeService; // Service xử lý logic liên quan đến OrderType

//    // ===================== Hiển thị form thêm ngôn ngữ =====================
//    @GetMapping("/language/add")
//    public String showLanguage(Model model, @RequestParam("orderTypeId") int orderTypeId) {
//        model.addAttribute("languages", new Language()); // Khởi tạo đối tượng ngôn ngữ mới
//        model.addAttribute("orderTypes", orderTypeService.getAllOrderTypes()); // Lấy danh sách OrderType
//        model.addAttribute("orderTypeId", orderTypeId); // Truyền orderTypeId để gắn với ngôn ngữ mới
//
//        return "Admin/Pages/Order/create-language"; // Trả về view form tạo mới ngôn ngữ
//    }

    // ===================== Xoá ngôn ngữ theo ID =====================
    @GetMapping("/language/delete/{id}")
    public String deleteLanguage(@PathVariable("id") int id,
            RedirectAttributes redirectAttributes) {
    	Language language = languageService.findById(id);
        OrderType orderType = orderTypeService.findById(language.getOrderType().getId());
    	
        try {
          
            languageService.deleteById(id); // Xoá ngôn ngữ theo ID
            redirectAttributes.addFlashAttribute("success", "Xóa thành công"); // Gửi thông báo thành công
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("danger", "Xóa thất bại"); 
        }

        return "redirect:/admin/order-type/" + orderType.getId(); // Redirect về danh sách orderType
    }

    // ===================== Hiển thị danh sách ngôn ngữ theo orderType ID
//    @GetMapping("/language/list/{id}")
//    public String showListLanguage(Model model, @PathVariable("id") int id) {
//        OrderType orderType = orderTypeService.getOrderTypeById(id).orElse(null); // Lấy orderType theo ID
//        List<Language> languages = languageService.getLanguagesByOrderTypeId(id); // Lấy danh sách ngôn ngữ thuộc
//                                                                                  // orderType
//        model.addAttribute("languages1", languages); // Gửi dữ liệu sang view
//        model.addAttribute("orderType", orderType);
//        System.out.println("Languages" + languages); // Log để kiểm tra dữ liệu
//        return "Admin/Pages/Order/edit-ordertype"; // Trả về view chỉnh sửa orderType và danh sách ngôn ngữ
//    }

    // ===================== Tạo mới ngôn ngữ =====================
    @PostMapping("/language/save")
    public String createLanguage(@ModelAttribute("langguage") Language language,
            RedirectAttributes redirectAttributes, @RequestParam("orderTypeId") int orderTypeId) {

        try {
            OrderType orderType = orderTypeService.getOrderTypeById(orderTypeId).orElse(null);
            language.setOrderType(orderType);
            languageService.saveLanguage(language); 

            // Gửi thông báo thành công
            redirectAttributes.addFlashAttribute("success","Lưu thành công!");

        } catch (Exception e) {
            // Nếu có lỗi, hiển thị thông báo thất bại
            redirectAttributes.addFlashAttribute("danger", "Lưu thất bại!");
        }

        return "redirect:/admin/order-type/" + orderTypeId; // Quay về danh sách orderType
    }

//    // ===================== Cập nhật ngôn ngữ =====================
//    @PostMapping("/language/update")
//    public String updeadLanguage(@ModelAttribute("language") Language language, RedirectAttributes redirectAttributes) {
//        try {
//            // Kiểm tra nếu tên ngôn ngữ bị trùng
//            if (languageService.checkname(language.getName())) {
//                redirectAttributes.addFlashAttribute("danger", "Tên Ngôn ngữ đã tồn tại");
//                return "redirect:/admin/order-type";
//            }
//
//            // Tìm ngôn ngữ cần cập nhật
//            Language Language1 = languageService.getLanguageById(language.getId());
//
//            // Cập nhật thông tin ngôn ngữ
//            Language1.setName(language.getName());
//            Language1.setPrice(language.getPrice());
//            Language1.setPromotionalPrice(language.getPromotionalPrice());
//            languageService.saveLanguage(Language1); // Lưu lại DB
//
//            redirectAttributes.addFlashAttribute("success", "Cập nhật Thành Công"); // Gửi thông báo thành công
//        } catch (Exception e) {
//            redirectAttributes.addFlashAttribute("danger", "Cập nhật Thất Bại"); // Gửi thông báo lỗi
//            return "redirect:/admin/language";
//        }
//
//        return "redirect:/admin/order-type"; // Quay về danh sách orderType
//    }
}
