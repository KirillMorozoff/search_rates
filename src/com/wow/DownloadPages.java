package com.wow;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DownloadPages{

    static String html;
    static List<String> allhtml = new ArrayList<>();

    public static void download(String url) {
        Document doc;
        try {
            doc = Jsoup.connect(url).get();
            html = doc.outerHtml();
            allhtml.add(html);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void downloadAllPages(String keyWord)
    {
        download("https://www.goodreads.com/search?utf8=%E2%9C%93&query=" + keyWord);
        String pages = "";
        String pattern = "(?<=<[d][i][v]>)([\\s\\S]*?)(?=<\\/[d][i][v]>)";
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(html);
        while(m.find()) {
            pages = html.substring(m.start(), m.end());
        }

        List<String> pageNumbers = new ArrayList<>();
        String pattern2 = "(?<=<[a][ ][h][r][e][f][=][\"][\\/][s][e][a][r][c][h][?][p][a][g][e][=])([\\s\\S]*?)(?=<\\/[a]>)";
        Pattern p2 = Pattern.compile(pattern2);
        Matcher m2 = p2.matcher(pages);
        while(m2.find()) {
            pageNumbers.add(pages.substring(m2.start(), m2.end()));
        }

        int numberOfPages = Integer.parseInt(pageNumbers.get(pageNumbers.size()-1).replaceAll("[\\S]+[>]+", ""));
        System.out.println("Количество поисковых страниц: " + numberOfPages);

        allhtml.add(html);


        for (int i = 0; i < numberOfPages; i++)
        {
            download("https://www.goodreads.com/search?page=" + i + "&query=" + keyWord + "&tab=books&utf8=%E2%9C%93");
            System.out.print(i + " ");
        }

    }


}
