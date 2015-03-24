package sitemap;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by Taras S. on 16.03.15.
 */
@RequestMapping("")
@Controller
public class MainController {

    @RequestMapping(value = {"", "/"}, method = RequestMethod.GET)
    public String index() {
        return protocol();
    }

    @RequestMapping(value = "protocol.html", method = RequestMethod.GET)
    public String protocol() {
        return "index";
    }

    @RequestMapping(value = "generate", method = RequestMethod.POST)
    public String generateSitemap(@RequestParam("url") String url) {
        System.out.println(url);
        return "index";
    }

}
