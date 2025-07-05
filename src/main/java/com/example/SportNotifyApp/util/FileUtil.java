package com.example.SportNotifyApp.util;

import org.jsoup.nodes.Document;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileUtil {

    public static void saveDocumentToHtml(Document doc, String path) {
        try {
            File file = new File(path);
            file.getParentFile().mkdirs(); // Tạo thư mục nếu chưa có

            try (FileWriter writer = new FileWriter(file)) {
                writer.write(doc.outerHtml()); // Ghi nội dung HTML
                System.out.println("📄 Document saved to: " + file.getAbsolutePath());
            }

        } catch (IOException e) {
            System.err.println("❌ Error writing HTML to file: " + e.getMessage());
        }
    }
}
