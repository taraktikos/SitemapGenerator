package sitemap.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@Service
public class CrawlerService {

    private Set<String> linksSetFirstStep = new HashSet<>();
    private Set<String> linksSetSecondStep = new HashSet<>();
    private Set<String> linksSetThirdStep = new HashSet<>();

    public Set<String> collectLinksFromUrl(String url) throws IOException {
        linksSetFirstStep.add(url);
        Document doc = Jsoup.connect(url).get();
        Elements links = doc.select("a[href]");
        for (Element link : links) {
            String href = link.attr("abs:href");
            if (href.startsWith(url)) {
                linksSetFirstStep.add(href.split("#")[0]);
            }
        }
        System.out.println("First size = " + linksSetFirstStep.size());
        for (String link : linksSetFirstStep) {
            doc = Jsoup.connect(link).get();
            links = doc.select("a[href]");
            for (Element l : links) {
                String href = l.attr("abs:href");
                if (href.startsWith(url) && !linksSetFirstStep.contains(href)) {
                    linksSetSecondStep.add(href.split("#")[0]);
                }
            }
        }
        System.out.println("Second size = " + linksSetSecondStep.size());
        for (String link : linksSetSecondStep) {
            doc = Jsoup.connect(link).get();
            links = doc.select("a[href]");
            for (Element l : links) {
                String href = l.attr("abs:href");
                if (href.startsWith(url) && !linksSetFirstStep.contains(href) && !linksSetSecondStep.contains(href)) {
                    linksSetThirdStep.add(href.split("#")[0]);
                }
            }
        }
        linksSetSecondStep.addAll(linksSetThirdStep);
        linksSetFirstStep.addAll(linksSetSecondStep);
        return linksSetFirstStep;
    }
}
