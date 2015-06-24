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

    @RequestMapping(value = "/doc/{docName}/edit", method = RequestMethod.GET)
    public String viewEditor(@PathVariable String docName, DustModel model) {
        model.put("docName", docName);
        model.put("content", content);
        return "editor";
    }

    @RequestMapping(value = "/doc/{docName}/save", method = RequestMethod.POST)
    public String save(@PathVariable String docName,
                       @RequestParam String content) {
        this.content = content;
        return "redirect:/doc/" + docName;
    }

    @RequestMapping(value = "/doc/create", method = RequestMethod.GET)
    public String viewEditor(DustModel model) {
        model.put("docName", "");
        model.put("content", "");
        return "editor";
    }

    @RequestMapping(value = "/doc/save", method = RequestMethod.POST)
    public String create(@RequestParam String docName,
                         @RequestParam String content) {
        this.content = content;
        return "redirect:/doc/" + docName;
    }

    @RequestMapping(value = "/doc/{docName}", method = RequestMethod.GET)
    public String view(@PathVariable String docName, DustModel model) {
        model.put("docName", docName);
        model.put("content", content);
        return "view";
    }

}
