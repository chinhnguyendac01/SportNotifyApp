package com.example.SportNotifyApp.crawler;

import com.example.SportNotifyApp.model.Match;
import com.example.SportNotifyApp.repository.MatchRepository;
import com.example.SportNotifyApp.util.ExcelUtil;

// import com.example.SportNotifyApp.util.FileUtil;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

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
            // Ngày hôm sau
            LocalDate matchDate = LocalDate.now().plusDays(1);
            String dateStr = matchDate.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            String url = "https://www.espn.com/soccer/schedule/_/date/" + dateStr;

            Document doc = Jsoup.connect(url).get();

            // Ghi HTML ra file
            // FileUtil.saveDocumentToHtml(doc, "D:/backend/sport_notify_app/doc.html");

            // Tất cả giải đấu
            Elements leagueDivs = doc.select(".mt3 > div");
             List<Match> savedMatches = new ArrayList<>();
            for (Element leagueDiv : leagueDivs) {
                // Tên giải đấu
                String league = leagueDiv.selectFirst(".Table__Title") != null
                        ? leagueDiv.selectFirst(".Table__Title").text()
                        : "Unknown League";

                Elements matchRows = leagueDiv.select("table.Table > tbody.Table__TBODY > tr");
               
                for (Element row : matchRows) {
                    try {
                        String teamA = row.select(".events__col.Table__TD a:nth-of-type(2)").text();
                        String teamB = row.select(".colspan__col.Table__TD a:nth-of-type(2)").text();
                        String time = row.select(".date__col.Table__TD a:nth-of-type(1)").text();

                        if (teamA.isEmpty() || teamB.isEmpty() || time.isEmpty())
                            continue;

                        // Tránh trùng lặp
                        if (matchRepository.existsByMatchDateAndTeamAAndTeamB(matchDate, teamA, teamB)) {
                            continue;
                        }

                        Match match = Match.builder()
                                .matchDate(matchDate)
                                .league(league)
                                .teamA(teamA)
                                .teamB(teamB)
                                .time(time)
                                .build();

                        matchRepository.save(match);
                        savedMatches.add(match);
                        System.out.printf("✅ Saved: %s vs %s at %s (%s)%n", teamA, teamB, time, league);

                    } catch (Exception ex) {
                        System.err.println("⚠️ Skipping row due to parse error: " + ex.getMessage());
                    }
                }
               
            }
             // Ghi file Excel sau khi đã duyệt xong tất cả match của 1 giải đấu
                if (!savedMatches.isEmpty()) {
                    List<String> headers = List.of("Date", "League", "Team A", "Team B", "Time");
                    List<String> fields = List.of("matchDate", "league", "teamA", "teamB", "time");
                    ExcelUtil.writeToExcel(savedMatches, headers, fields, "D:/backend/sport_notify_app/matches.xlsx");
                }
        } catch (Exception e) {
            System.err.println("❌ Error while crawling: " + e.getMessage());
        }
    }
}
