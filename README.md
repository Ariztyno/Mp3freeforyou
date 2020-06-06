# Mp3freeforyou
Ứng dụng nghe nhạc online sử dụng 000webhost làm nơi lưu trữ các file nhạc (.mp3) và lưu trữ database (MySQL)
Ứng dụng nghe nhạc sử dụng thư viện thư viện Retrofit với sự hỗ trợ của các thư viện OkHtttp (Xử lý các yêu cầu mạng) và Gson (đổi các dữ liệu JSON thành các đối tượng java)
Retrofit sẽ gửi hỗ trợ việc gửi request (http url) đến dịch vụ rest api của trang web (thông qua việc dịch các api thành các interface của java) tại đó file php thuộc đường dẫn url sẽ xử lý yêu cầu và trả về dữ liệu lấy được từ database sau khi đã qua chuyển đổi thành các đối tượng JSON.
