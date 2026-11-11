# Figma SVG UI Screens Export (Bao gồm Portrait & Landscape Orientation)

Tất cả các file SVG giao diện chuẩn độ phân giải Android Pixel cùng script Python tái tạo đã được gom gọn đầy đủ trong thư mục:
📂 **`figma_assets/`**

Bạn có thể **kéo thả trực tiếp (Drag & Drop)** toàn bộ các file `.svg` này vào Figma hoặc mở trong trình duyệt/Figma để chụp ảnh màn hình chèn vào file Word/PDF report.

---

## 📱 Danh Sách Các Màn Hình Đã Tạo (9 Màn Hình):

| File Name | Tỉ lệ ViewBox | Mô tả màn hình | Trạng thái hiển thị |
| :--- | :--- | :--- | :--- |
| **`01_home_catalogue_light.svg`** | 412 × 892 (Dọc) | **Màn hình chính (Catalogue - Light Mode)** | Header, Credits: 500, Favourites section (Toyota Camry), Search bar, Sort Rating, Hero Featured Card xe Toyota Camry (đầy đủ spec circles, sao rating 4.8, nút Rent), Danh sách Popular Cars swipe ngang. |
| **`02_home_catalogue_dark.svg`** | 412 × 892 (Dọc) | **Màn hình chính (Catalogue - Dark Mode OLED)** | Theme đen tuyền (True Pitch Black `#000000`), card màu charcoal cao cấp, switch Dark Mode bật ON (biểu tượng trăng khuyết), độ tương phản cao cho màn hình OLED. |
| **`03_search_filter_active.svg`** | 412 × 892 (Dọc) | **Tìm kiếm xe theo tên/model (Search Active)** | Thanh search đang gõ từ khoá `"Tesla"`, nút xoá `X`, badge kết quả `"1 vehicle matching"`, hiển thị duy nhất card Tesla Model 3 (4.9 sao, Dual Motors, 75 credits/day). |
| **`04_rental_details_valid.svg`** | 412 × 892 (Dọc) | **Chi tiết thuê xe hợp lệ (RentalActivity)** | Nút Back, ảnh xe lớn, bảng thông số, thẻ tính toán chi phí (50 credits × 3 ngày = 150 credits), Slider chọn thời gian kéo ở mức **3 Days**, nút Cancel & Confirm Booking. |
| **`05_rental_details_error.svg`** | 412 × 892 (Dọc) | **Validation Error - Vượt hạn mức 400 credits** | Xe BMW 3 Series (90 credits/day), slider ở mức **5 Days** (tổng 450 credits), xuất hiện **Banner báo lỗi đỏ**: *"Limit Exceeded: Total cost (450 credits) exceeds individual rental limit of 400 credits"*, nút Confirm bị vô hiệu hoá. |
| **`06_booking_success_rented.svg`** | 412 × 892 (Dọc) | **Đặt xe thành công & Hoàn trả xe (Active Rental)** | Toast thông báo xanh lá *"Booking successful! 150 credits deducted for 3 day(s)"*, Số dư cập nhật giảm còn **350 credits**, mục **Active Rentals (1 active)** với xe Toyota Camry kèm nút **"Return"** để hoàn trả xe phục hồi credit. |
| **`07_home_catalogue_vietnamese.svg`** | 412 × 892 (Dọc) | **Đa ngôn ngữ Tiếng Việt (Vietnamese Localisation)** | Biểu tượng ngôn ngữ chuyển sang **VI**, toàn bộ tiêu đề *"Thuê Xe Ô Tô"*, *"Số dư: 500"*, *"Xe Yêu Thích"*, nút *"Thuê Xe"*, *"Xe Phổ Biến"*, đơn vị *"điểm/ngày"*, thông số tiếng Việt. |
| **`08_home_catalogue_landscape.svg`** | 892 × 412 (Ngang) | **Màn hình chính khi XOAY NGANG (Landscape Catalogue)** | **Bố cục 2 panel song song (Dual-Pane)**: Bên trái là Thẻ số dư tài khoản 500 credits + Thanh tìm kiếm / lọc sắp xếp + Mini dashboard (Favourites & Active Rentals). Bên phải là Hàng xe Popular dạng swipe ngang (card rộng 236px, ảnh 135px) kèm nút điều hướng mũi tên và nút Rent. |
| **`09_rental_details_landscape.svg`** | 892 × 412 (Ngang) | **Màn hình thuê xe khi XOAY NGANG (Landscape Rental)** | **Bố cục 2 cột (Side-by-side Dual-Pane)**: Cột trái tóm tắt xe (ảnh xe rộng 370px, thông số, mô tả, sao rating). Cột phải gồm bảng tính giá chi tiết, thanh Slider kéo ngày thuê, dòng hiển thị số dư còn lại sau thuê (350 credits), 2 nút bấm Cancel Booking và Confirm Booking đặt cạnh nhau. |

---

## 🛠 Hướng Dẫn Sử Dụng Với Figma:
1. Mở Figma (Desktop App hoặc trên web figma.com).
2. Tạo một Project hoặc File mới.
3. Mở File Explorer trên máy tính, vào thư mục:
   `D:\COS30017-Software Development for Mobile Devices\GitHub-Repo\ASM 2\figma_assets\`
4. Kéo thả trực tiếp từng file `.svg` vào bảng vẽ (Canvas) của Figma:
   - Các layer vector (Button, Card, Text, Icon, Slider, Stars, Status Bar) được nhóm rõ ràng, giữ nguyên vector sắc nét, có thể tuỳ biến màu sắc hoặc font chữ.
   - Hình ảnh xe chất lượng cao được nhúng sẵn nguyên bản dạng Base64, không bị đứt link.
5. Trong Figma, bạn có thể nhấn phím `Shift + E` hoặc chọn Export sang định dạng **PNG (2x / 3x)** để chèn vào báo cáo Assignment 2.
