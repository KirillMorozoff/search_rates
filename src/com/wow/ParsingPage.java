package com.wow;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static com.wow.DownloadPages.allhtml;

public  class ParsingPage {
    static List<String> allBooks = new ArrayList<>();
    static List<String> booksNamesAndAuthors = new ArrayList<>();
    static List<Double> booksRatings = new ArrayList<>();
    static List<Double> booksRates = new ArrayList<>();
    static List<String> result = new ArrayList<>();
    static List<String> result2 = new ArrayList<>();
    static Double WR = 0.0;

    static void makingAllBookList()
    {
        String pattern = "(?<=<[t][r][ ][i][t][e][m][s][c][o][p][e])([\\s\\S]*?)(?=<[!][-])";
        Pattern p = Pattern.compile(pattern);

        for (int i = 0; i < allhtml.size(); i++) {
            String result = allhtml.get(i);
            Matcher m = p.matcher(result);
            while (m.find()) {
                allBooks.add(result.substring(m.start(), m.end()));
            }
        }
        System.out.println("");
        for (int i = 0; i < allBooks.size(); i++)
        {
            Document doc = Jsoup.parse(allBooks.get(i));

            Element bookName = doc.select("a.booktitle").first();
            Element bookAuthor = doc.select("a.authorName").first();
            Element bookRating = doc.select("span.minirating").first();

            String name = bookName.text();
            String author = bookAuthor.text();
            String rating = bookRating.text();
            booksNamesAndAuthors.add(name + " " + "by" + " " + author);

            String pattern2 = "[\\d]+[,][\\d]+[\\W][r][a][t][i][n][g][s]|[\\d]+[\\W][r][a][t][i][n][g]";
            Pattern p2 = Pattern.compile(pattern2);
            Matcher m2 = p2.matcher(rating);
            while(m2.find()) {
                String bookR = rating.substring(m2.start(), m2.end());
                bookR = bookR.substring(0, bookR.length()-7).replace(",", "");
                booksRates.add(Double.parseDouble(bookR));
            }

            String pattern1 = "[\\d][.][\\d]+";
            Pattern p1 = Pattern.compile(pattern1);
            Matcher m1 = p1.matcher(rating);
            while(m1.find()) {
                booksRatings.add(Double.parseDouble(rating.substring(m1.start(), m1.end())));
            }

        }
        System.out.println(booksNamesAndAuthors.size() + " " + booksRatings.size() + " " + booksRates.size());

        for (int i = 0; i < booksNamesAndAuthors.size(); i++)
        {
            Double v = booksRatings.get(i);
            Double n = 500.0;
            Double R = booksRates.get(i);
            Double C = 3.3;
            WR = (v / (v+n)) * R + (n / (v+n)) * C;
            double newDouble = new BigDecimal(WR).setScale(1, RoundingMode.UP).doubleValue();
            String idmb = String.valueOf(newDouble).replace(".", "");
            if (Integer.parseInt(idmb) < 1000 && Integer.parseInt(idmb) > 100)
            {
                idmb = "0" + idmb;
            } else if (Integer.parseInt(idmb) < 100)
            {
                idmb = "00" + idmb;
            }
            result.add(idmb + " " + booksNamesAndAuthors.get(i));
        }

        Set<String> set = new HashSet<String>(result);
        result.clear();
        result.addAll(set);

        Collections.sort(result);
        Collections.reverse(result);



        for (int i = 0; i < result.size(); i++)
        {
            System.out.println(result.get(i));
        }


        System.out.println("Количество книг: " + allBooks.size());
    }
}
