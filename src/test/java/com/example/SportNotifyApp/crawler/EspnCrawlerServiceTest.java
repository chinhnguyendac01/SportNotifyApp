package com.example.SportNotifyApp.crawler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.test.annotation.Rollback;

@SpringBootTest
class EspnCrawlerServiceTest {

    @Autowired
    private EspnCrawlerService crawlerService;

    @Test
    @Rollback(false)
    void testCrawl() {
        crawlerService.crawl();
        // Có thể assert thêm nếu crawl trả về dữ liệu hoặc lưu vào DB
        // Kiểm tra số lượng match mới được lưu vào DB
        //mvn test  will run this test
    }
}