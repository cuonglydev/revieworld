package com.example.Controller.User;

import com.example.Entity.User;
import com.example.Entity.UserBank;
import com.example.Service.UserBankService;
import com.example.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/account/bank")
public class UserBankController {
    private static final Logger logger = LoggerFactory.getLogger(UserBankController.class);

    @Autowired
    private UserBankService userBankService;

    @Autowired
    private UserService userService;

    @GetMapping("")
    public String getBankPage(Model model) {
        try {
            User currentUser = userService.getCurrentUser();
            if (currentUser == null) {
                return "redirect:/login";
            }

            List<UserBank> userBanks = userBankService.findByUserId(currentUser.getId());
            model.addAttribute("userBanks", userBanks);
            
            return "User/Pages/Account/Fragment/account-detail-element :: account-detail";
        } catch (Exception e) {
            logger.error("Error loading bank page: ", e);
            return "error/500";
        }
    }

    @GetMapping("/list")
    @ResponseBody
    @Transactional(readOnly = true)
    public ResponseEntity<?> getUserBanks() {
        try {
            logger.info("=== Start getting bank list ===");
            User currentUser = userService.getCurrentUser();
            
            if (currentUser == null) {
                logger.error("No current user found");
                return ResponseEntity.status(401).body(createErrorResponse("Vui lòng đăng nhập để xem thông tin ngân hàng"));
            }
            
            logger.info("Getting banks for user ID: {}", currentUser.getId());
            List<UserBank> banks = userBankService.findByUserId(currentUser.getId());
            logger.info("Found {} banks for user {}", banks.size(), currentUser.getId());
            
            return ResponseEntity.ok(banks);
        } catch (Exception e) {
            logger.error("Error getting user banks: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(createErrorResponse("Có lỗi xảy ra khi tải thông tin ngân hàng"));
        }
    }

    @PostMapping("/add")
    @ResponseBody
    @Transactional
    public ResponseEntity<?> addUserBank(@RequestBody UserBank userBank) {
        try {
            logger.info("=== Start adding bank account ===");
            logger.info("Received request body: {}", userBank);
            
            User currentUser = userService.getCurrentUser();
            logger.info("Current user details - ID: {}, Email: {}", 
                currentUser != null ? currentUser.getId() : "null",
                currentUser != null ? currentUser.getEmail() : "null");
            
            if (currentUser == null) {
                logger.error("No current user found");
                return ResponseEntity.status(401).body(createErrorResponse("Vui lòng đăng nhập để thêm ngân hàng"));
            }

            // Set user for the bank account
            userBank.prepareForSave(currentUser);
            logger.info("Bank data after preparation: {}", userBank);

            try {
                // Validate the bank account data
                userBank.validate();
                logger.info("Validation passed successfully");
            } catch (IllegalArgumentException e) {
                logger.warn("Validation failed: {}", e.getMessage());
                return ResponseEntity.badRequest().body(createErrorResponse(e.getMessage()));
            }

            // Check for duplicate account number
            if (userBankService.existsByUserIdAndAccountNumber(currentUser.getId(), userBank.getAccountNumber())) {
                logger.warn("Duplicate account number found");
                return ResponseEntity.badRequest().body(createErrorResponse("Số tài khoản/địa chỉ ví đã tồn tại"));
            }

            // Save the bank account
            UserBank savedBank = userBankService.save(userBank);
            logger.info("Bank account saved successfully: {}", savedBank);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Thêm " + (userBank.getMethod().equals("BANK") ? "tài khoản ngân hàng" : "ví USDT") + " thành công");
            response.put("bank", savedBank);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error adding bank account: ", e);
            String errorMessage = e.getMessage();
            if (errorMessage == null || errorMessage.isEmpty()) {
                errorMessage = "Có lỗi xảy ra khi thêm tài khoản ngân hàng";
            }
            return ResponseEntity.internalServerError().body(createErrorResponse(errorMessage));
        }
    }

    @GetMapping("/{id}")
    @ResponseBody
    @Transactional(readOnly = true)
    public ResponseEntity<?> getBankById(@PathVariable int id) {
        try {
            User currentUser = userService.getCurrentUser();
            if (currentUser == null) {
                return ResponseEntity.status(401).body(createErrorResponse("Vui lòng đăng nhập"));
            }

            Optional<UserBank> bank = userBankService.findById(id);
            if (bank.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            // Verify ownership
            if (bank.get().getUser().getId() != currentUser.getId()) {
                return ResponseEntity.status(403).body(createErrorResponse("Bạn không có quyền xem thông tin này"));
            }

            return ResponseEntity.ok(bank.get());
        } catch (Exception e) {
            logger.error("Error getting bank details: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(createErrorResponse("Có lỗi xảy ra khi tải thông tin ngân hàng"));
        }
    }

    @PutMapping("/update/{id}")
    @ResponseBody
    @Transactional
    public ResponseEntity<?> updateUserBank(@PathVariable int id, @RequestBody UserBank userBank) {
        try {
            User currentUser = userService.getCurrentUser();
            if (currentUser == null) {
                return ResponseEntity.status(401).body(createErrorResponse("Vui lòng đăng nhập"));
            }

            Optional<UserBank> existing = userBankService.findById(id);
            if (existing.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            UserBank bank = existing.get();
            if (bank.getUser().getId() != currentUser.getId()) {
                return ResponseEntity.status(403).body(createErrorResponse("Bạn không có quyền cập nhật thông tin này"));
            }

            // Update fields
            bank.setMethod(userBank.getMethod());
            bank.setBankName(userBank.getBankName());
            bank.setAccountNumber(userBank.getAccountNumber());
            bank.setAccountName(userBank.getAccountName());

            // Validate
            bank.validate();

            UserBank updatedBank = userBankService.save(bank);
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Cập nhật thông tin thành công");
            response.put("bank", updatedBank);
            
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(createErrorResponse(e.getMessage()));
        } catch (Exception e) {
            logger.error("Error updating bank: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(createErrorResponse("Có lỗi xảy ra khi cập nhật thông tin"));
        }
    }

    @DeleteMapping("/delete/{id}")
    @ResponseBody
    @Transactional
    public ResponseEntity<?> deleteUserBank(@PathVariable int id) {
        try {
            logger.info("Received request to delete bank ID: {}", id);
            
            User currentUser = userService.getCurrentUser();
            if (currentUser == null) {
                logger.error("Unauthorized access attempt to delete bank");
                return ResponseEntity.status(401).body(createErrorResponse("Vui lòng đăng nhập để xóa ngân hàng"));
            }

            Optional<UserBank> existing = userBankService.findById(id);
            if (existing.isEmpty()) {
                logger.error("Bank not found with ID: {}", id);
                return ResponseEntity.notFound().build();
            }

            UserBank bank = existing.get();
            if (bank.getUser().getId() != currentUser.getId()) {
                logger.error("User {} attempted to delete bank {} belonging to user {}", 
                    currentUser.getId(), id, bank.getUser().getId());
                return ResponseEntity.status(403).body(createErrorResponse("Bạn không có quyền xóa tài khoản ngân hàng này"));
            }

            userBankService.deleteById(id);
            logger.info("Successfully deleted bank {}", id);
            
            Map<String, String> response = new HashMap<>();
            response.put("message", "Xóa " + ("BANK".equals(bank.getMethod()) ? "tài khoản ngân hàng" : "ví USDT") + " thành công");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error deleting bank {}: {}", id, e.getMessage(), e);
            return ResponseEntity.internalServerError().body(createErrorResponse("Có lỗi xảy ra khi xóa tài khoản ngân hàng"));
        }
    }

    @GetMapping("/test/current-user")
    @ResponseBody
    public ResponseEntity<?> testCurrentUser() {
        try {
            User currentUser = userService.getCurrentUser();
            if (currentUser == null) {
                return ResponseEntity.status(401).body(createErrorResponse("No current user"));
            }
            Map<String, Object> response = new HashMap<>();
            response.put("userId", currentUser.getId());
            response.put("email", currentUser.getEmail());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(createErrorResponse(e.getMessage()));
        }
    }

    @GetMapping("/test/list-all")
    @ResponseBody
    public ResponseEntity<?> testListAll() {
        try {
            List<UserBank> allBanks = userBankService.findAll();
            return ResponseEntity.ok(Map.of(
                "count", allBanks.size(),
                "banks", allBanks
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(createErrorResponse(e.getMessage()));
        }
    }

    @PostMapping("/test/add")
    @ResponseBody
    public ResponseEntity<?> testAdd(@RequestBody UserBank userBank) {
        try {
            User currentUser = userService.getCurrentUser();
            if (currentUser == null) {
                return ResponseEntity.status(401).body(createErrorResponse("No current user"));
            }
            
            userBank.setUser(currentUser);
            UserBank savedBank = userBankService.save(userBank);
            
            return ResponseEntity.ok(Map.of(
                "message", "Bank added successfully",
                "bank", savedBank
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(createErrorResponse(e.getMessage()));
        }
    }

    @PostMapping("/simple-add")
    public String addBankAccountSimple(
            @RequestParam String method,
            @RequestParam(required = false) String bankName,
            @RequestParam String accountNumber,
            @RequestParam(required = false) String accountName,
            RedirectAttributes redirectAttributes) {
        
        logger.info("=== Simple form bank account addition ===");
        logger.info("Received form data - Method: {}, Bank Name: {}, Account Number: {}, Account Name: {}",
                method, bankName, accountNumber, accountName);
        
        try {
            // Get current user
            User currentUser = userService.getCurrentUser();
            if (currentUser == null) {
                logger.error("No authenticated user");
                redirectAttributes.addFlashAttribute("error", "Vui lòng đăng nhập để thêm ngân hàng");
                return "redirect:/account/bank";
            }
            
            // Create UserBank object
            UserBank userBank = new UserBank();
            userBank.setMethod(method);
            userBank.setBankName(bankName);
            userBank.setAccountNumber(accountNumber);
            userBank.setAccountName(accountName);
            userBank.setUser(currentUser);
            
            // Validate data directly
            try {
                if (method == null || method.trim().isEmpty()) {
                    throw new IllegalArgumentException("Phương thức không được để trống");
                }
                
                if (!method.matches("^(BANK|USDT)$")) {
                    throw new IllegalArgumentException("Phương thức không hợp lệ");
                }
                
                if (accountNumber == null || accountNumber.trim().isEmpty()) {
                    throw new IllegalArgumentException("Số tài khoản/địa chỉ ví không được để trống");
                }
                
                if ("BANK".equals(method)) {
                    if (bankName == null || bankName.trim().isEmpty()) {
                        throw new IllegalArgumentException("Tên ngân hàng không được để trống");
                    }
                    
                    if (accountName == null || accountName.trim().isEmpty()) {
                        throw new IllegalArgumentException("Tên tài khoản không được để trống");
                    }
                    
                    // Basic validation for account number
                    if (!accountNumber.trim().matches("^\\d+$")) {
                        throw new IllegalArgumentException("Số tài khoản chỉ được chứa số");
                    }
                } else if ("USDT".equals(method)) {
                    // For USDT, set bank name and account name to null
                    userBank.setBankName(null);
                    userBank.setAccountName(null);
                }
                
                // Check for duplicate account
                if (userBankService.existsByUserIdAndAccountNumber(currentUser.getId(), accountNumber)) {
                    throw new IllegalArgumentException("Số tài khoản/địa chỉ ví đã tồn tại");
                }
                
            } catch (IllegalArgumentException e) {
                logger.warn("Validation failed: {}", e.getMessage());
                redirectAttributes.addFlashAttribute("error", e.getMessage());
                return "redirect:/account/bank";
            }
            
            // Save to database
            UserBank savedBank = userBankService.save(userBank);
            logger.info("Bank account saved successfully with ID: {}", savedBank.getId());
            
            // Set success message
            redirectAttributes.addFlashAttribute("success", "Thêm " + 
                    ("BANK".equals(method) ? "tài khoản ngân hàng" : "ví USDT") + " thành công");
            
            return "redirect:/account/bank";
            
        } catch (Exception e) {
            logger.error("Error adding bank account: ", e);
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra: " + e.getMessage());
            return "redirect:/account/bank";
        }
    }

    @GetMapping("/simple")
    public String getSimpleBankPage(Model model) {
        try {
            User currentUser = userService.getCurrentUser();
            if (currentUser == null) {
                return "redirect:/login";
            }

            List<UserBank> userBanks = userBankService.findByUserId(currentUser.getId());
            model.addAttribute("userBanks", userBanks);
            
            return "User/Pages/Account/simple-bank-form";
        } catch (Exception e) {
            logger.error("Error loading simple bank page: ", e);
            return "error/500";
        }
    }

    @GetMapping("/edit/{id}")
    public String editBankForm(@PathVariable int id, Model model, RedirectAttributes redirectAttributes) {
        try {
            User currentUser = userService.getCurrentUser();
            if (currentUser == null) {
                return "redirect:/login";
            }
            
            Optional<UserBank> bankOpt = userBankService.findById(id);
            if (bankOpt.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Không tìm thấy tài khoản ngân hàng");
                return "redirect:/account/bank/simple";
            }
            
            UserBank bank = bankOpt.get();
            if (bank.getUser().getId() != currentUser.getId()) {
                redirectAttributes.addFlashAttribute("error", "Bạn không có quyền chỉnh sửa tài khoản này");
                return "redirect:/account/bank/simple";
            }
            
            model.addAttribute("bank", bank);
            model.addAttribute("editing", true);
            
            List<UserBank> userBanks = userBankService.findByUserId(currentUser.getId());
            model.addAttribute("userBanks", userBanks);
            
            return "User/Pages/Account/simple-bank-form";
        } catch (Exception e) {
            logger.error("Error loading edit form: ", e);
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra: " + e.getMessage());
            return "redirect:/account/bank/simple";
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteBank(@PathVariable int id, RedirectAttributes redirectAttributes) {
        try {
            User currentUser = userService.getCurrentUser();
            if (currentUser == null) {
                return "redirect:/login";
            }
            
            Optional<UserBank> bankOpt = userBankService.findById(id);
            if (bankOpt.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Không tìm thấy tài khoản ngân hàng");
                return "redirect:/account/bank/simple";
            }
            
            UserBank bank = bankOpt.get();
            if (bank.getUser().getId() != currentUser.getId()) {
                redirectAttributes.addFlashAttribute("error", "Bạn không có quyền xóa tài khoản này");
                return "redirect:/account/bank/simple";
            }
            
            userBankService.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Xóa tài khoản thành công");
            
            return "redirect:/account/bank/simple";
        } catch (Exception e) {
            logger.error("Error deleting bank: ", e);
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra: " + e.getMessage());
            return "redirect:/account/bank/simple";
        }
    }

    private Map<String, String> createErrorResponse(String message) {
        Map<String, String> response = new HashMap<>();
        response.put("message", message);
        return response;
    }
} 