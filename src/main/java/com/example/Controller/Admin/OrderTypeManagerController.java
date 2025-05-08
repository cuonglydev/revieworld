package com.example.Controller.Admin;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.Entity.OrderType;
import com.example.Service.OrderTypeService;

@Controller
@RequestMapping("/admin")
public class OrderTypeManagerController {
    @Autowired
    private OrderTypeService orderTypeService;
    private static final String UPLOAD_DIR = "src/main/resources/static/uploads/";

    @GetMapping("/ordertype")
    public String getAllOrderTypes(Model model) {
        List<OrderType> orderTypes = orderTypeService.getAllOrderTypes();
        model.addAttribute("orderTypes", orderTypes);
        return "Admin/Pages/OrderType/order-type";
    }

    // Hiển thị form tạo OrderType mới
    @GetMapping("/ordertype/new")
    public String showCreateForm(Model model) {
        model.addAttribute("orderType", new OrderType());
        return "Admin/Pages/OrderType/create";
    }

    // Hiển thị form sửa OrderType
    // @GetMapping("/ordertype/edit/{id}")
    // public String showEditForm(@PathVariable int id, Model model) {
    // OrderType orderType = orderTypeService.getOrderTypeById(id)
    // .orElseThrow(() -> new IllegalArgumentException("Invalid order type Id:" +
    // id));
    // model.addAttribute("orderType", orderType);
    // return "Admin/Pages/OrderType/edit";
    // }

    // Xử lý form tạo/sửa OrderType
    @PostMapping("/ordertype/save")
    public String saveOrderType(@ModelAttribute OrderType orderType,
            @RequestParam("photoFile") MultipartFile photoFile,
            RedirectAttributes redirectAttributes) {
        try {
            // Xử lý upload ảnh
            if (!photoFile.isEmpty()) {
                // Tạo thư mục uploads nếu chưa tồn tại
                File uploadDir = new File(UPLOAD_DIR);
                if (!uploadDir.exists()) {
                    uploadDir.mkdirs();
                }

                // Tạo tên file duy nhất
                String fileName = UUID.randomUUID().toString() + "_" + photoFile.getOriginalFilename();
                Path filePath = Paths.get(UPLOAD_DIR + fileName);
                Files.write(filePath, photoFile.getBytes());

                // Lưu tên file vào trường photo
                orderType.setPhoto(fileName);
            }

            // Nếu không upload ảnh mới, giữ nguyên ảnh cũ (trong trường hợp chỉnh sửa)
            if (orderType.getId() != 0 && photoFile.isEmpty()) {
                OrderType existingOrderType = orderTypeService.getOrderTypeById(orderType.getId())
                        .orElseThrow(() -> new IllegalArgumentException("Invalid order type Id:" + orderType.getId()));
                orderType.setPhoto(existingOrderType.getPhoto());
            }

            orderTypeService.saveOrderType(orderType);
            redirectAttributes.addFlashAttribute("success", "Lưu thành công");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("danger", "Lưu thất bại: " + e.getMessage());
        }
        return "redirect:/admin/order-type";
    }

    // Xử lý xóa OrderType
    @GetMapping("/ordertype/delete/{id}")
    public String deleteOrderType(@PathVariable int id, RedirectAttributes redirectAttributes) {
        try {
            orderTypeService.deleteOrderType(id);
            redirectAttributes.addFlashAttribute("success", "Xóa thành công");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("danger", "Xóa thất bại");
        }
        return "redirect:/admin/order-type";
    }
}