package com.example.Controller.Admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.Entity.DefaultRank;
import com.example.Entity.Rank;
import com.example.Service.DefaultRankService;
import com.example.Service.RankService;

@Controller
@RequestMapping("/admin")
public class RankManagerController {
    @Autowired
    RankService rankService;
    
    @Autowired
    private DefaultRankService defaultRankService;

    @GetMapping("/rank")
    public String getAllRanks(Model model) {
        List<Rank> ranks = rankService.getAllRanks();
        model.addAttribute("ranks", ranks);
        
        DefaultRank defaultRank = defaultRankService.findById(1);
        model.addAttribute("defaultRank", defaultRank);
        return "Admin/Pages/Rank/rank";
    }
    
    @PostMapping("/default-rank/save")
    public String saveDefaultRank(@ModelAttribute DefaultRank defaultRank, RedirectAttributes redirectAttributes) {
    	try {
    		defaultRankService.save(defaultRank);
    		redirectAttributes.addFlashAttribute("success", "Cập nhật hạng mặc định thành công!");
    	}catch (Exception e) {
    		redirectAttributes.addFlashAttribute("danger", "Cập nhật hạng mặc định thất bại!");
			// TODO: handle exception
		}
    	return "redirect:/admin/rank";
    }

//    // Hiển thị form tạo Rank mới
//    @GetMapping("/new")
//    public String showCreateForm(Model model) {
//        model.addAttribute("rank", new Rank());
//        return "Admin/Pages/Rank/create";
//    }

//    // Hiển thị form sửa Rank
//    @GetMapping("/edit/{id}")
//    public String showEditForm(@PathVariable int id, Model model) {
//        Rank rank = rankService.getRankById(id)
//                .orElseThrow(() -> new IllegalArgumentException("Invalid rank Id:" + id));
//        model.addAttribute("rank", rank);
//        return "Admin/Pages/Rank/edit"; // Đảm bảo đường dẫn đúng
//    }

    @PostMapping("/rank/save")
    public String createRank(@ModelAttribute Rank rank, RedirectAttributes redirectAttributes) {
        try {
            rankService.saveRank(rank);
            redirectAttributes.addFlashAttribute("success", "Lưu thành công");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("danger", "Lưu thất bại");
        }
        return "redirect:/admin/rank"; // Đảm bảo đường dẫn đúng
    }

//    @GetMapping("/rank/new")
//    public String createRankPage() {
//        return "Admin/Pages/Rank/create"; // Đảm bảo đường dẫn đúng
//    }

    // Xử lý xóa Rank
    @GetMapping("/rank/delete/{id}")
    public String deleteRank(@PathVariable int id, RedirectAttributes redirectAttributes) {
        try {
            rankService.deleteRank(id);
            redirectAttributes.addFlashAttribute("success", "Xóa thành công");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("danger", "Xóa thất bại");
        }
        return "redirect:/admin/rank";
    }
}
