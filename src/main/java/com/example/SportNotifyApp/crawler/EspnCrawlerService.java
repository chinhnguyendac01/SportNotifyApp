package com.example.SportNotifyApp.crawler;

import com.example.SportNotifyApp.model.Match;
import com.example.SportNotifyApp.repository.MatchRepository;
import com.example.SportNotifyApp.util.FileUtil;

import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EspnCrawlerService {

    private final MatchRepository matchRepository;

    public void crawl() {
        try {
            // Lấy ngày hôm nay + 1
            LocalDate tomorrow = LocalDate.now().plusDays(1);
            String dateStr = tomorrow.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            String url = "https://www.espn.com/soccer/schedule/_/date/" + dateStr;

            Document doc = Jsoup.connect(url).get();

            FileUtil.saveDocumentToHtml(doc, "D:/backend/sport_notify_app/doc.html");
            
            Elements sections = doc.select("section"); // mỗi ngày

            for (Element section : sections) {
                String date = section.select("h2").text();

                Elements cards = section.select(".Card");

                for (Element card : cards) {
                    String league = card.select(".Card__Header").text();
                    Elements matches = card.select("table tbody tr");

                    for (Element match : matches) {
                        Elements teamNames = match.select("td.team-name");
                        if (teamNames.size() < 2)
                            continue;

                        String teamA = teamNames.first().text();
                        String teamB = teamNames.last().text();
                        String time = match.select("td.match-time").text();

                        // Convert date String to LocalDate
                        java.time.LocalDate localDate = java.time.LocalDate.parse(date);

                        // Tránh insert trùng lặp
                        if (matchRepository.existsByMatchDateAndTeamAAndTeamB(localDate, teamA, teamB))
                            continue;

                        Match newMatch = Match.builder()
                                .matchDate(localDate)
                                .league(league)
                                .teamA(teamA)
                                .teamB(teamB)
                                .time(time)
                                .build();

                        matchRepository.save(newMatch);
                        System.out.printf("✅ Saved: %s vs %s at %s (%s)%n", teamA, teamB, time, league);
                    }
                }
            }

        } catch (Exception e) {
            System.err.println("❌ Error while crawling: " + e.getMessage());
        }
    }
}