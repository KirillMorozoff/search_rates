package com.wow;

import java.io.IOException;
import static com.wow.ParsingPage.makingAllBookList;

public class Main {



    public static void main(String[] args) throws IOException, InterruptedException {
        DownloadPages.downloadAllPages("surfing");
        makingAllBookList();

    }

}
