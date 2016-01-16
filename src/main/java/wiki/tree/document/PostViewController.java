package wiki.tree.document;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import wiki.tree.document.converter.AsciidoctorDocumentConverter;
import wiki.tree.document.domain.Document;
import wiki.tree.document.domain.Tag;
import wiki.tree.document.repository.DocumentRepository;
import wiki.tree.document.repository.TagRepository;

import java.util.List;

/**
 * @author chanwook
 */
@Controller
public class PostViewController {

    @Autowired
    private DocumentRepository dr;

    @Autowired
    private TagRepository tr;

    @RequestMapping(value = "/p/{name}", method = RequestMethod.GET)
    public String view(@PathVariable String name, ModelMap model) {
        final Document doc = dr.findByName(name);
        String html = new AsciidoctorDocumentConverter(doc).convert();

        final List<Tag> tag = tr.findByReference(doc.getId());

        model.put("name", name);
        model.put("document", doc);
        model.put("tag", tag);
        model.put("_html", html);
        return "view";
    }
}
