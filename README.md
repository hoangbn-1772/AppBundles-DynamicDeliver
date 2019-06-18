# App Bundles-Dynamic Deliver
# App Bundles
- App Bundles là một định dạng upload mới bao gồm tất cả các code và resource được compile từ app, ảnh hưởng đến cách chúng ta xây dựng và cấu trúc ứng dụng của mình theo định dạng module.
- Mặc dù gói này vẫn chứa code và resource, sự khác biệt lớn nhất là trách nhiệm xây dựng APK được chuyển lên Google Play.

## Lợi ích của App Bundle
  - Dynamic Delivery: Sử dụng App Bundle dể tạo APK tối ưu cho từng loại thiết bị người dùng, vì vậy với từng thiết bị chỉ cần tải xuống code và resource cần thiết để run app. Kích thước ứng dụng nhỏ hơn. Ví dụ: Nếu bạn đã thiết lập English là ngôn ngữ mặc định thì không cần ngôn ngữ khác.
  - Không cần quản lý thủ công nhiều APKs: Không phải tạo nhiều APK cho các device có độ phân giải màn hình khác nhau. Chúng ta chỉ cần tải lên một artifact duy nhất cùng với tất cả các tài nguyên của ứng dụng, việc xây dựng và cung cấp cho người dùng sẽ được xử lý tự động.
  - Dynamic Feature Module (Phân phối động): Cho phép người dùng tải xuống và cài đặt thêm các tính năng khi có yêu cầu. Điều này cho phép chúng ta làm giảm kích thước ban đầu của app. Các tính năng này nằm ở các module khác nhau và sử dụng thư viện Play Core Library để tải xuống các module này.
  - Giảm kích thước APK: Sử dụng cơ chế Split APK, Google Play có thể chia một app lớn thành nhiều gói nhỏ, các gói này được cài đặt theo yêu cầu người dùng.
  - Support Instant App: Người dùng có thể chạy các feature module mà không cần phải cài đặt app. Tham khảo Instant App 
  <a href="https://viblo.asia/p/android-instant-app-buoc-dot-pha-cho-trai-nghiem-nguoi-dung-XL6lAA0mlek">Tại đây</a>
