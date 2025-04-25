function checkPasswordsMatch() {
    const password = document.getElementById('password').value;
    const confirmPassword = document.getElementById('confirmPassword').value;
    const toastEl = document.getElementById('passwordError'); // toast element
    const toast = new bootstrap.Toast(toastEl); // tạo đối tượng Toast

    if (password !== confirmPassword) {
      toast.show(); // gọi hiển thị
      return false; // Ngăn form submit
    }

    return true;
  }

  // Tùy chọn: kiểm tra trong lúc người dùng đang gõ
  document.getElementById('confirmPassword').addEventListener('input', function () {
    // không cần show toast ở đây, chỉ check để xóa lỗi nếu muốn
  });