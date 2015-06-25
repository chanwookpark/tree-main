package wiki.tree.main.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import r2.dustjs.spring.DustModel;
import wiki.tree.main.domain.Document;
import wiki.tree.main.repository.DocumentRepository;

/**
 * @author chanwook
 */
@Controller
public class NavigationPageController {

    @Autowired
    private DocumentRepository r;

    @RequestMapping("/home")
    public String viewHome() {
        return "home";
    }

    @RequestMapping("/post")
    public String post(Pageable pageRequest, DustModel model) {
        final Page<Document> page = r.findAll(pageRequest);
        model.put("page", page);
        return "post";
    }
}
