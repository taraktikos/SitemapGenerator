package sitemap.service;

import com.redfin.sitemapgenerator.WebSitemapGenerator;
import org.springframework.stereotype.Service;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Set;

@Service
public class SitemapService {

    public void generateXml(String url, Set<String> links, File baseDir, String fileNamePrefix) throws Exception {
        if (!baseDir.exists()) {
            baseDir.mkdirs();
        }
        WebSitemapGenerator wsg = WebSitemapGenerator.builder(url, baseDir).fileNamePrefix(fileNamePrefix).build();
        for (String link : links) {
            wsg.addUrl(link);
        }
        wsg.write();
    }

}
