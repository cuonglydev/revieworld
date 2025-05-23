package com.example.Controller.Admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@Controller
@RequestMapping("/admin")
public class ContentManageController {

<<<<<<< Updated upstream
	
=======
    @Value("${static-folder}")
    private String staticFolder;
    @Autowired
    private UploadService uploadService;
    @Autowired
    private ContentSevice contentSevice;

    @GetMapping("/content")
    public String showContent(Model model, RedirectAttributes redirectAttributes) {
        try {
            List<Content> contents = contentSevice.getAllContent();
            model.addAttribute("contents", contents);
            redirectAttributes.addFlashAttribute("success", "Hiển Thị Danh sách Thành Công");
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("danger", "Hiển Thị Thất Bại: " + e.getMessage());
        }

        return "Admin/Pages/Content/content-detail";

    }

    @GetMapping("/content/delete/{id}")
    public String DeleteContent(@PathVariable("id") int id, RedirectAttributes redirectAttributes) {
        try {
            contentSevice.deleteContent(id);
            redirectAttributes.addFlashAttribute("success", "Xóa Thành Công");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("danger", "Xóa Thất Bại: " + e.getMessage());

        }
        return "redirect:/admin/content";
    }

    @PostMapping("/content/update")
    public String saveContent(@ModelAttribute Content content, @RequestParam("contentNote") String contentNote,
            @RequestParam("photoFile") MultipartFile photoFile,
            RedirectAttributes redirectAttributes) throws IOException {
        try {
        	System.out.println(contentNote);
        	content.setContent(contentNote);
            if (content.getId() == 0) {
                content.setCreatedAt(new Date());
                if (!photoFile.isEmpty()) {
                    String filePhoto = uploadService.saveFile(photoFile, "images");
                    content.setPhoto(staticFolder + "images/" + filePhoto);
                } else {
                    content.setPhoto("images/default.png");
                }
                contentSevice.saveContent(content);
                redirectAttributes.addFlashAttribute("success", "Thêm mới thành công");
            }

            else {
                Content getContent = contentSevice.getContentById(content.getId());

                if (!photoFile.isEmpty()) {
                    String filePhoto = uploadService.saveFile(photoFile, "images");
                    content.setPhoto(staticFolder + "images/" + filePhoto);
                } else {
                    content.setPhoto(getContent.getPhoto());
                }
                content.setCreatedAt(getContent.getCreatedAt());

                contentSevice.saveContent(content);
                redirectAttributes.addFlashAttribute("success", "Cập Nhập thành công");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("danger", "Thêm mới thất bại");

        }

        return "redirect:/admin/content";
    }

    @GetMapping("/content/edit/{id}")
    public String showEditContent(@PathVariable("id") int id, Model model) {
        Content content = contentSevice.getContentById(id);
        model.addAttribute("content", content);
        return "Admin/Pages/Content/edit-content";
    }

    @PostMapping("/content/edit")
    public String editContent(@ModelAttribute("Content") Content content,
            @RequestParam("photoFile") MultipartFile photoFile,
            RedirectAttributes redirectAttributes) throws IOException {

        try {
            Content content1 = contentSevice.getContentById(content.getId());

            if (!photoFile.isEmpty()) {
                String filePhoto = uploadService.saveFile(photoFile, "images");
                content.setPhoto(staticFolder + "images/" + filePhoto);
            } else {

                content.setPhoto(content1.getPhoto());
            }
            content.setCreatedAt(content1.getCreatedAt());

            contentSevice.saveContent(content);
            redirectAttributes.addFlashAttribute("success", "Cập Nhập thành công");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("danger", "Cập nhật  thất bại");

        }

        return "redirect:/admin/content";
    }
>>>>>>> Stashed changes
}
