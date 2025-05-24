package com.example.Controller.Admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;

import com.example.Common.Slugify;
import com.example.Entity.OrderType;
import com.example.Entity.Support;
import com.example.Service.SupportService;
import com.example.Service.UploadService;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequestMapping("/admin")
public class SupportController {
    @Autowired
    private SupportService supportService;
    @Value("${upload.dir}")
    private String uploadDir; // Đường dẫn thư mục upload từ application.properties
    // private Slugify slugify;
    @Autowired
    private UploadService uploadService;

    @Value("${static-folder}")
    private String staticFolder;

    @GetMapping("/support/list")
    public String showgetAllsSupports(Model model) {
        model.addAttribute("supports", supportService.getAllsSupports());
        return "Admin/Pages/Setting/Support";
    }

    @GetMapping("/support/delete/{id}")
    public String deleteSupport(@PathVariable int id, Model model, RedirectAttributes redirectAttributes) {
        try {
            supportService.deleteSupport(id);
            redirectAttributes.addFlashAttribute("success", "Xoá Thành Công");
        } catch (Exception e) {
            // TODO: handle exception
            redirectAttributes.addFlashAttribute("danger", "Xoá Thất bại");
        }
        return "redirect:/admin/support/list";
    }

    @PostMapping("/support/add")
    public String postMethodName(@ModelAttribute Support support,
            @RequestParam(value = "photoFile") MultipartFile photoFile, RedirectAttributes redirectAttributes) {

        try {
            if (!photoFile.isEmpty()) {
                String filePhoto = uploadService.saveFile(photoFile, "images"); // Lưu ảnh và lấy tên file
                support.setPhoto(staticFolder + "images/" + filePhoto); // Thiết lập đường dẫn ảnh
            } else {
                support.setPhoto("images/default.png"); // Gán ảnh mặc định nếu không có file
            }
            supportService.SaveSupport(support);
            redirectAttributes.addFlashAttribute("success", "Tạo mới thành công");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("danger", "Tạo mới thất bại");
        }
        return "redirect:/admin/support/list";
    }

    @PostMapping("/support/update")
    public String UpdateSupport(@ModelAttribute Support support,
            @RequestParam(value = "photoFile") MultipartFile photoFile,
            RedirectAttributes redirectAttributes) {
        try {

            Support existing = supportService.findById(support.getId());
            // Nếu có file ảnh mới => cập nhật
            if (!photoFile.isEmpty()) {
                String filePhoto = uploadService.saveFile(photoFile, "images");
                support.setPhoto(staticFolder + "images/" + filePhoto);
            } else {

                support.setPhoto(existing.getPhoto());
            }
            supportService.SaveSupport(support);
            redirectAttributes.addFlashAttribute("success", "Cập Nhật thành công");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("danger", "Cập Nhật thất bại");
        }

        return "redirect:/admin/support/list";

    }

}
