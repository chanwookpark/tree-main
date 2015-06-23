package wiki.tree.main.editor;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import r2.dustjs.spring.DustModel;

/**
 * @author chanwook
 */
@Controller
public class DocumentEditorController {

    @RequestMapping(value = "/doc/{docId}/edit", method = RequestMethod.GET)
    public String viewEditor(@PathVariable("docId") String docId, DustModel model) {
        model.put("content", "블라블라 컨텐츠");
        return "editor";
    }
}
