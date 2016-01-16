package wiki.tree.navigation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import wiki.tree.document.domain.Document;
import wiki.tree.document.repository.DocumentRepository;
import wiki.tree.document.service.UserService;

/**
 * @author chanwook
 */
@Controller
public class NavigationPageController {

    @Autowired
    private DocumentRepository r;

    @Autowired
    private UserService us;

    @RequestMapping("/home")
    public String viewHome() {
        return "home";
    }

    @RequestMapping("/post")
    public String post(Pageable pageRequest, ModelMap model) {
        final Page<Document> page = r.findAll(pageRequest);
        //TODO 개선하기..
        us.replaceUserIdToName(page.getContent());
        model.put("postList", page.getContent());
        return "post";
    }

}
