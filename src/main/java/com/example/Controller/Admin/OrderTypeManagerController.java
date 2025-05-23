package com.example.Controller.Admin;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.Common.Slugify;
import com.example.Entity.Language;
import com.example.Entity.Order;
import com.example.Entity.OrderType;
import com.example.Service.LanguageService;
import com.example.Service.OrderService;
import com.example.Service.OrderTypeService;
import com.example.Service.UploadService;

@Controller
@RequestMapping("/admin")
public class OrderTypeManagerController {

    @Autowired
    private OrderTypeService orderTypeService; // Service xử lý logic OrderType

    @Autowired
    private UploadService uploadService; // Service dùng để upload file ảnh

    @Autowired
    private LanguageService languageService;

    @Autowired
    private OrderService orderService;

    private Slugify slugify;

    @Value("${upload.dir}")
    private String uploadDir; // Đường dẫn thư mục upload từ application.properties

    @Value("${static-folder}")
    private String staticFolder; // Thư mục tĩnh để lưu đường dẫn hình ảnh hiển thị trên web

    // // ========================= Hiển thị form tạo mới OrderType
    // // =========================
    // @GetMapping("/ordertype/new")
    // public String showCreateForm(Model model) {
    // model.addAttribute("orderType", new OrderType()); // Tạo đối tượng mới để
    // binding form
    // return "Admin/Pages/OrderType/create"; // Trả về view form tạo mới
    // }

    // ========================= Hiển thị danh sách OrderType
    // =========================
    @GetMapping("/ordertype")
    public String getAllOrderTypes(Model model, RedirectAttributes redirectAttributes) {
        try {
            List<OrderType> orderTypes = orderTypeService.getAllOrderTypes(); // Lấy danh sách orderType
            model.addAttribute("orderTypes", orderTypes);
            redirectAttributes.addFlashAttribute("success", "Hiển Thị Danh sách Thành Công");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("danger", "Hiển Thị Thất Bại: " + e.getMessage());
        }
        return "Admin/Pages/Order/order-type"; // Trả về view danh sách OrderType
    }

    // ========================= Xóa một OrderType theo ID =========================
    @GetMapping("/ordertype/delete/{id}")
    public String deleteOrderType(@PathVariable int id, RedirectAttributes redirectAttributes) {
        List<Order> orders = orderService.findAllByOrderTypeId(id);
        try {
            for (Order order : orders) {
                Order getOrder = orderService.findById(order.getId());
                getOrder.setOrderType(null);
                orderService.save(getOrder);
            }
            List<Language> languages = languageService.findAllByOrderTypeId(id);
            for (Language language : languages) {
                languageService.deleteById(language.getId());
            }

            orderTypeService.deleteOrderType(id); // Gọi service để xóa
            redirectAttributes.addFlashAttribute("success", "Xóa thành công");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("danger", "Xóa thất bại");
        }
        return "redirect:/admin/order-type"; // Quay lại trang danh sách
    }

    // ========================= Hiển thị form chỉnh sửa OrderType
    // =========================
    @GetMapping("/order-type/{id}")
    public String showEditForm(@PathVariable int id, Model model) {
        OrderType orderType = orderTypeService.getOrderTypeById(id).orElse(null);
        model.addAttribute("orderType", orderType); // Đưa dữ liệu orderType vào model để binding lên form

        List<Language> languages = languageService.findAllByOrderTypeId(id);
        model.addAttribute("languages", languages);

        return "Admin/Pages/Order/order-type-detail";
    }

    // ========================= Tạo mới hoặc cập nhật OrderType
    // =========================
    @PostMapping("/ordertype/update")
    public String saveOrderType(@ModelAttribute OrderType orderType,
            @RequestParam(value = "photoFile") MultipartFile photoFile,
            RedirectAttributes redirectAttributes) {
        try {
            // Chuyển tên sang định dạng slug
            String slug = slugify.toSlug(orderType.getName());
            orderType.setSlug(slug);

            // =========== THÊM MỚI ===========
            if (orderType.getId() == 0) {
                // Kiểm tra slug đã tồn tại chưa
                if (orderTypeService.checkeachother(slug)) {
                    redirectAttributes.addFlashAttribute("danger", "Slug đã tồn tại! Vui lòng chọn tên khác.");
                    return "redirect:/admin/order-type";
                }

                orderType.setCreatedAt(new Date()); // Thiết lập ngày tạo mới

                // Kiểm tra nếu có file ảnh
                if (!photoFile.isEmpty()) {
                    String filePhoto = uploadService.saveFile(photoFile, "images"); // Lưu ảnh và lấy tên file
                    orderType.setPhoto(staticFolder + "images/" + filePhoto); // Thiết lập đường dẫn ảnh
                } else {
                    orderType.setPhoto("images/default.png"); // Gán ảnh mặc định nếu không có file
                }

                orderTypeService.saveOrderType(orderType); // Lưu vào database
                redirectAttributes.addFlashAttribute("success", "Thêm mới thành công");

                // =========== CẬP NHẬT ===========
            } else {

                OrderType existing = orderTypeService.getOrderTypeById(orderType.getId()).orElse(null);
                // Nếu có file ảnh mới => cập nhật
                if (!photoFile.isEmpty()) {
                    String filePhoto = uploadService.saveFile(photoFile, "images");
                    orderType.setPhoto(staticFolder + "images/" + filePhoto);
                } else {

                    orderType.setPhoto(existing.getPhoto());
                }

                orderType.setCreatedAt(existing.getCreatedAt());

                orderTypeService.saveOrderType(orderType); // Lưu lại thông tin đã cập nhật
                redirectAttributes.addFlashAttribute("success", "Cập nhật thành công");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("danger", "Lỗi: " + e.getMessage()); // Xử lý lỗi
        }

        return "redirect:/admin/order-type/" + orderType.getId(); // Quay lại trang danh sách OrderType
    }
}
