package com.plasticmobile;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class Challenge {

    public static void main(String[] args) throws IOException {
        while (true) {
            String search = null;
            boolean isValid = false;
            while (!isValid) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                System.out.print("Enter your search for toronto craigslist: ");

                search = reader.readLine();

                if (StringUtils.isBlank(search)) {
                    System.out.println("");
                    System.err.println("Search can not be empty!");
                } else {
                    isValid = true;
                }
            }

            System.out.println("");

            /*
                get the search response html body from craiglist
             */
            Document doc = Jsoup.connect("https://toronto.craigslist.ca/search/sss?query=" + search.trim() + "&sort=rel").get();

            /*
                after read the response html body
                <p class="result-info">
                    <span class="icon icon-star" role="button">
                        <span class="screen-reader-text">favorite this post</span>
                    </span>
                    <time class="result-date" datetime="2018-06-29 14:23" title="Fri 29 Jun 02:23:06 PM">Jun 29</time>
                    <a href="https://toronto.craigslist.ca/tor/app/d/humidifier-table-top-good/6622331049.html" data-id="6622331049" class="result-title hdrlnk">Humidifier (table top) - good condition!</a>
                    <span class="result-meta">
                        <span class="result-price">$15</span>
                        <span class="result-hood"> (Scarborough)</span>
                        <span class="result-tags"> pic
                            <span class="maptag" data-pid="6622331049">map</span>
                        </span>
                        <span class="banish icon icon-trash"
                              role="button">
                            <span class="screen-reader-text">hide this posting</span>
                        </span>
                        <span class="unbanish icon icon-trash red" role="button" aria-hidden="true"></span>
                        <a href="#" class="restore-link">
                            <span class="restore-narrow-text">restore</span>
                            <span class="restore-wide-text">restore this posting</span>
                        </a>
                    </span>
                </p>

                so basically just parse the html tags and find the values we need.
                 <time class="result-date" datetime="2018-06-29 14:23" title="Fri 29 Jun 02:23:06 PM">Jun 29</time>
                 <a href="https://toronto.craigslist.ca/tor/app/d/humidifier-table-top-good/6622331049.html" data-id="6622331049" class="result-title hdrlnk">Humidifier (table top) - good condition!</a>

             */
            Elements resultInfos = doc.select("p.result-info");

            JSONArray result = new JSONArray();
            for (Element e : resultInfos) {
                Element title = e.children().select("a.result-title").first();
                Element date = e.children().select("time.result-date").first();

                JSONObject json = new JSONObject();
                if (title != null) {
                    json.put("title", title.text());
                }
                if (date != null) {
                    json.put("date", date.text());
                }

                result.put(json);

            }
            System.out.println(result);
        }


    }
}
