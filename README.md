# App Bundles-Dynamic Deliver
# App Bundles
- App Bundles là một định dạng upload mới bao gồm tất cả các code và resource được compile từ app, ảnh hưởng đến cách chúng ta xây dựng và cấu trúc ứng dụng của mình theo định dạng module.
- Mặc dù gói này vẫn chứa code và resource, sự khác biệt lớn nhất là trách nhiệm xây dựng APK được chuyển lên Google Play.

## Lợi ích của App Bundle
  - Dynamic Delivery: Sử dụng App Bundle dể tạo APK tối ưu cho từng loại thiết bị người dùng, vì vậy với từng thiết bị chỉ cần tải xuống code và resource cần thiết để run app. Kích thước ứng dụng nhỏ hơn. Ví dụ: Nếu bạn đã thiết lập English là ngôn ngữ mặc định thì không cần ngôn ngữ khác.
  - Không cần tạo và quản lý thủ công nhiều APKs: Không phải tạo nhiều APK cho các device có độ phân giải màn hình khác nhau. Chúng ta chỉ cần tải lên một artifact duy nhất cùng với tất cả các tài nguyên của ứng dụng, việc xây dựng và cung cấp cho người dùng sẽ được xử lý tự động. Nghĩa là App Bundle sẽ tạo ra nhiều ra APK tối ưu hóa cho từng thiết bị cài đặt nó.
  - Dynamic Feature Module (Phân phối động): Cho phép người dùng tải xuống và cài đặt thêm các tính năng khi có yêu cầu. Điều này cho phép chúng ta làm giảm kích thước ban đầu của app. Các tính năng này nằm ở các module khác nhau và sử dụng thư viện Play Core Library để tải xuống các module này.
  - Giảm kích thước APK: Sử dụng cơ chế Split APK, Google Play có thể chia một app lớn thành nhiều gói nhỏ, các gói này được cài đặt theo yêu cầu người dùng.
  - Support Instant App: Người dùng có thể chạy các feature module mà không cần phải cài đặt app. Tham khảo Instant App 
  <a href="https://viblo.asia/p/android-instant-app-buoc-dot-pha-cho-trai-nghiem-nguoi-dung-XL6lAA0mlek">Tại đây</a>
## Định dạng App Bundle
- App Bundle là một file (có phần mở rộng .aab, không thể cài đặt trên device) mà bản upload lên Google Play để được support *Dynamic Delivery*.
- App Bundle là các tệp nhị phân được ký kết, sắp xếp code và resource của ứng dụng thành các module.

<img src="images/app_bundle_format.xml">

- Các phần được tô màu xanh như drawable, values, lib đại diện cho code và resource mà Google Play sử dụng để tạo APK cấu hình cho từng Module.
- Mặc dù các tệp này chúng ta có thể tìm thấy trong APK, nhưng nó lại phục vụ một mục đích khác. Ngoài ra nó còn chứa một số tệp mà trong APK không có
- Chi tiết:
  + base/, feature1/, feature2/: Đại diện cho một module khác nhau của app, và tất cả được chứa trong base directory của App Bundle. Tuy nhiên, thư mục cho <>dynamic module</b> được đặt tên theo chỉ định bởi thuộc tính split trong module manifest.
  + BUNDLE-METADATA/: Thư mục này bao gồm các file metadata chứa các thông tin hữu ích cho các tool hoặc app store. Các file metadata như vậy có thể bao gồm ánh xạ Proguard, danh sách đầy đủ các tệp *DEX* của app. Các file trong mục này không được đóng gói vào APK của app.
  + Module Protocol Buffer(.pb): Các file này cung cấp metadata để mô tả nội dung của từng module cho app store. Ví dụ: *BundleConfig.pb* cung cấp thông tin của chính nó chẳng hạn như version nào của các tool được sử dụng để xây dựng app bundle *native.pb*, *resources.pb* mô tả code và resources trong mỗi module, rất hữu ích khi Google Play tối ưu hóa APK cho các cấu hình thiết bị khác nhau. *assets.pb* 
- Note: Nếu sử dụng để tạo nhiều phiên bản ứng dụng từ một app project và mỗi version sử dụng applicationID duy nhất, bạn cần phải tạo một App Bundle riêng biệt cho từng version.

## Tham khảo
- https://developer.android.com/guide/app-bundle
- https://medium.com/google-developer-experts/exploring-the-android-app-bundle-ca16846fa3d7
- https://medium.com/mindorks/android-app-bundle-6c65ce8105a1
- https://viblo.asia/p/gioi-thieu-ve-android-app-bundle-m68Z00r6ZkG
- https://viblo.asia/p/co-ban-ve-android-app-bundle-ByEZkNdqKQ0
- https://techtalk.vn/co-ban-ve-android-app-bundle.html
