package wiki.tree.main.home;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 'HOME' 페이지를 위한 컨트롤러
 *
 * @author chanwook
 */
@Controller
public class HomeController {

    @RequestMapping("/home")
    public String viewHome() {
        return "home";
    }
}
