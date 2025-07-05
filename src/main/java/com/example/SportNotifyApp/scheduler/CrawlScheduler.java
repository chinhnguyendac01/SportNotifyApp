package com.example.SportNotifyApp.scheduler;

import com.example.SportNotifyApp.crawler.EspnCrawlerService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CrawlScheduler {

    private final EspnCrawlerService crawlerService;

    @Scheduled(cron = "0 * * * * *") // mỗi giờ
    public void runCrawlerJob() {
        System.out.println("🕒 Running ESPN crawler job...");
        crawlerService.crawl();
    }
}