package sitemap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import sitemap.service.CrawlerService;
import sitemap.service.SitemapService;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

@RequestMapping("")
@Controller
public class MainController {

    @Autowired
    CrawlerService crawlerService;

    @Autowired
    SitemapService sitemapService;

    @Value("${sitemap.baseDir}")
    private String siteMapBaseDir;

    @RequestMapping(value = {"", "/"}, method = RequestMethod.GET)
    public String index() {
        return protocol();
    }

    @RequestMapping(value = "protocol.html", method = RequestMethod.GET)
    public String protocol() {
        return "index";
    }

    @RequestMapping(value = "generate", method = RequestMethod.POST)
    public String generateSitemap(@RequestParam("url") String url, Model model) {
        System.out.println(url);

        File baseDir = new File(siteMapBaseDir);
        String date = new SimpleDateFormat("yyyyMMddhhmm").format(new Date());
        String fileNamePrefix = "sitemap" + date;
        try {
            Set<String> links = crawlerService.collectLinksFromUrl(url);
            System.out.println("Total links count: " + links.size());
            sitemapService.generateXml(url, links, baseDir, fileNamePrefix);
        } catch (Exception e) {
            e.printStackTrace();
        }
        model.addAttribute("fileName", fileNamePrefix + ".xml");
        return "index";
    }

    @RequestMapping(value = "download", method = RequestMethod.GET)
    public void download(@RequestParam("fileName") String fileName, HttpServletResponse response) {
        File file = new File(siteMapBaseDir + fileName);
        response.setContentType("application/xml");
        response.setHeader("Content-Disposition", "attachment; filename=" + file.getName());
        response.setContentLength((int) file.length());
        try {
            FileCopyUtils.copy(new FileInputStream(file), response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
