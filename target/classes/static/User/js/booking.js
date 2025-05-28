// ================== File Preview & Swiper Handling ==================
let selectedFiles = [];

const fileInput = document.getElementById('fileInput');
const previewWrapper = document.getElementById('previewWrapper');
const numberInput = document.getElementById('numberInput');
const form = document.querySelector('form');
const confirmBtn = document.getElementById('confirmBtn');
const turnNumber = document.getElementById('turnNumber');
const languageSelect = document.getElementById('languageSelect');
const basePriceInput = document.getElementById('basePrice');
const percentDiscount = document.getElementById('percentDiscount');
const priceDisplays = document.querySelectorAll('[data-price-display]');
const discountAmounts = document.querySelectorAll('[data-discount-amount]');
const totalPayments = document.querySelectorAll('[data-total-payment]');
const txtInput = document.getElementById('txtFileInput');
const txtPreviewButton = document.getElementById('txtPreviewTrigger');
const txtModalBody = document.getElementById('txtModalBody');

fileInput.addEventListener('change', function (event) {
  const quantity = parseInt(numberInput.value, 10) || 0;
  const files = Array.from(event.target.files);

  if (!quantity) {
    alert("Vui lòng nhập số lượng trước khi chọn ảnh.");
    fileInput.value = '';
    return;
  }

  if (files.length > quantity) {
    alert(`Chỉ được chọn tối đa ${quantity} ảnh`);
    fileInput.value = '';
    return;
  }

  selectedFiles = files;
  updatePreview();
  updateSwiper();
  updatePrice();
  updateTurnNumberMax();
  checkFormValidity();
});

numberInput.addEventListener('input', () => {
  numberInput.dataset.changed = true;
  const quantity = parseInt(numberInput.value, 10) || 0;
  selectedFiles = selectedFiles.slice(0, quantity);

  const dataTransfer = new DataTransfer();
  selectedFiles.forEach(file => dataTransfer.items.add(file));
  fileInput.files = dataTransfer.files;

  updatePreview();
  updateSwiper();
  updatePrice();
  updateTurnNumberMax();
  checkFormValidity();
});

function updatePreview() {
  previewWrapper.innerHTML = "";
  if (selectedFiles.length === 0) return;

  const container = document.createElement('div');
  container.style.position = "relative";
  container.style.width = "50px";
  container.style.height = "50px";
  container.style.cursor = "pointer";
  container.className = "me-2";
  container.setAttribute("data-bs-toggle", "modal");
  container.setAttribute("data-bs-target", "#imageModal");

  const img = document.createElement('img');
  img.src = URL.createObjectURL(selectedFiles[0]);
  img.style.width = "100%";
  img.style.height = "100%";
  img.style.objectFit = "cover";
  img.style.opacity = "0.5";
  img.style.border = "dashed 1px gray";
  img.className = "rounded";

  const countSpan = document.createElement('span');
  countSpan.textContent = `+${selectedFiles.length}`;
  countSpan.style.position = "absolute";
  countSpan.style.top = "50%";
  countSpan.style.left = "50%";
  countSpan.style.transform = "translate(-50%, -50%)";
  countSpan.style.color = "#000";

  container.appendChild(img);
  container.appendChild(countSpan);
  previewWrapper.appendChild(container);
}

let mainSwiper = null;
let thumbsSwiper = null;

function handleDeleteImage(indexToDelete) {
  selectedFiles.splice(indexToDelete, 1);

  const dataTransfer = new DataTransfer();
  selectedFiles.forEach(file => dataTransfer.items.add(file));
  fileInput.files = dataTransfer.files;

  updatePreview();
  updateSwiper();
  updatePrice();
  updateTurnNumberMax();
  checkFormValidity();
}

function updateSwiper() {
  const mainContainer = document.querySelector('.mySwiperMain');
  const thumbsContainer = document.querySelector('.mySwiperThumbs');

  mainContainer.innerHTML = "";
  thumbsContainer.innerHTML = "";

  selectedFiles.forEach((file, index) => {
    const url = URL.createObjectURL(file);

    const mainSlide = document.createElement('div');
    mainSlide.classList.add('swiper-slide');
    const wrapper = document.createElement('div');
    wrapper.style.position = 'relative';

    const mainImg = document.createElement('img');
    mainImg.src = url;
    mainImg.style.width = "100%";
    mainImg.style.height = "auto";
    mainImg.style.borderRadius = "10px";

    const deleteBtn = document.createElement('button');
    deleteBtn.innerHTML = '<i class="fa-solid fa-trash"></i>';
    
    deleteBtn.style.top = '5px';
    deleteBtn.style.right = '5px';
    deleteBtn.classList.add('btn', 'btn-danger', 'btn-sm', 'position-absolute');
    deleteBtn.addEventListener('click', (e) => {
      e.stopPropagation();
      handleDeleteImage(index);
    });

    wrapper.appendChild(mainImg);
    wrapper.appendChild(deleteBtn);
    mainSlide.appendChild(wrapper);
    mainContainer.appendChild(mainSlide);

    const thumbSlide = document.createElement('div');
    thumbSlide.classList.add('swiper-slide');
    const thumbImg = document.createElement('img');
    thumbImg.src = url;
    thumbImg.style.width = "100%";
    thumbImg.style.height = "auto";
    thumbSlide.appendChild(thumbImg);
    thumbsContainer.appendChild(thumbSlide);
  });

  if (mainSwiper) mainSwiper.destroy(true, true);
  if (thumbsSwiper) thumbsSwiper.destroy(true, true);

  thumbsSwiper = new Swiper(".mySwiperThumbs", {
    spaceBetween: 10,
    slidesPerView: 4,
    freeMode: true,
    watchSlidesProgress: true,
  });

  mainSwiper = new Swiper(".mySwiperMain", {
    spaceBetween: 10,
    navigation: true,
    thumbs: {
      swiper: thumbsSwiper,
    },
  });
}

function updatePrice() {
  const basePrice = parseFloat(basePriceInput.value) || 0;
  const quantity = parseInt(numberInput.value, 10) || 1;
  const totalPrice = basePrice * quantity;

  priceDisplays.forEach(display => {
    display.textContent = totalPrice.toLocaleString('en-US', { style: 'currency', currency: 'USD' });
  });

  const percent = parseFloat(percentDiscount.value) || 0;
  const discountAmount = (totalPrice * percent) / 100;
  discountAmounts.forEach(display => {
    display.textContent = discountAmount.toLocaleString('en-US', { style: 'currency', currency: 'USD' });
  });

  const totalPayment = totalPrice - discountAmount;
  totalPayments.forEach(display => {
    display.textContent = totalPayment.toLocaleString('en-US', { style: 'currency', currency: 'USD' });
  });
}

function updateTurnNumberMax() {
  const quantity = parseInt(numberInput.value, 10) || 1;
  turnNumber.max = quantity;

  const value = parseInt(turnNumber.value, 10);
  if (!isNaN(value)) {
    if (value > quantity) {
      turnNumber.value = quantity;
    }
  }
}

turnNumber.addEventListener('input', () => {
  updateTurnNumberMax();
});

languageSelect.addEventListener('change', () => {
  const selectedOption = languageSelect.options[languageSelect.selectedIndex];
  const price = selectedOption.getAttribute('data-price');
  basePriceInput.value = price || '';
  updatePrice();
  updateTurnNumberMax();
  checkFormValidity();
});

function checkFormValidity() {
  const quantity = parseInt(numberInput.value, 10) || 1;
  const turnValue = parseInt(turnNumber.value, 10);
  const isTurnValid = !isNaN(turnValue) && turnValue >= 1 && turnValue <= quantity;

  confirmBtn.disabled = !form.checkValidity() || !isTurnValid;
}

const inputs = form.querySelectorAll('input, select, textarea');
inputs.forEach(input => {
  input.addEventListener('input', checkFormValidity);
  input.addEventListener('change', checkFormValidity);
});
fileInput.addEventListener('change', checkFormValidity);
window.addEventListener('load', checkFormValidity);

txtInput.addEventListener('change', function (e) {
  const file = e.target.files[0];

  if (!file || !file.name.endsWith('.txt')) {
    txtPreviewButton.style.display = 'none';
    txtModalBody.textContent = 'Chỉ hỗ trợ file .txt';
	txtInput.value = ''; 
    return;
  }

  const reader = new FileReader();
  reader.onload = function (e) {
    txtModalBody.textContent = e.target.result;
    txtPreviewButton.style.display = 'block';
  };
  reader.readAsText(file, 'UTF-8');
});

document.addEventListener('DOMContentLoaded', () => {
  updatePrice();
  updateTurnNumberMax();
  checkFormValidity();
});
