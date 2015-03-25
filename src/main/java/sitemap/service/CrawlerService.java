package sitemap.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Service
public class CrawlerService {

    private Set<String> getLinks(Set<String> urls, String mainUrl) {
        Document doc;
        Elements links;
        Set<String> resultLinks = new HashSet<>();
        for (String url : urls) {
            try {
                doc = Jsoup.connect(url).get();
                links = doc.select("a[href]");
                for (Element l : links) {
                    String href = l.attr("abs:href");
                    if (href.startsWith(mainUrl)) {
                        resultLinks.add(href.split("#")[0]);
                    }
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
        return resultLinks;
    }

    public Set<String> collectLinksFromUrl(String mainUrl) {
        Set<String> firstLevel = getLinks(new HashSet<>(Collections.singletonList(mainUrl)), mainUrl);

        Set<String> secondLevel = getLinks(firstLevel, mainUrl);

        secondLevel.removeAll(firstLevel);
        Set<String> thirdLevel = getLinks(secondLevel, mainUrl);

        firstLevel.addAll(secondLevel);
        firstLevel.addAll(thirdLevel);
        return firstLevel;
    }

}
