package wiki.tree.main.editor;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import r2.dustjs.spring.DustModel;

/**
 * @author chanwook
 */
@Controller
public class DocumentEditorController {

    String content;

    @RequestMapping(value = "/doc/{docId}/edit", method = RequestMethod.GET)
    public String viewEditor(@PathVariable String docId, DustModel model) {
        model.put("docId", docId);
        model.put("content", content);
        return "editor";
    }

    @RequestMapping(value = "/doc/{docId}/save", method = RequestMethod.POST)
    public String save(@PathVariable String docId, @RequestParam("content") String content) {
        this.content = content;
        return "redirect:/doc/{docId}";
    }

    @RequestMapping(value = "/doc/{docId}", method = RequestMethod.GET)
    public String view(@PathVariable String docId, DustModel model) {
        model.put("docId", docId);
        model.put("content", content);
        return "view";
    }
}
