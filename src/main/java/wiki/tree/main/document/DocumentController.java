package wiki.tree.main.document;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import r2.dustjs.spring.DustModel;
import wiki.tree.main.document.domain.Document;
import wiki.tree.main.document.domain.Tag;
import wiki.tree.main.document.repository.DocumentRepository;
import wiki.tree.main.document.repository.TagRepository;
import wiki.tree.main.document.service.DocumentService;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

/**
 * @author chanwook
 */
@Controller
public class DocumentController {

    @Autowired
    private DocumentRepository dr;

    @Autowired
    private TagRepository tr;

    @Autowired
    private DocumentService ds;

    @RequestMapping(value = "/doc/edit/{docName}", method = RequestMethod.GET)
    public String viewEditor(@PathVariable String docName, DustModel model) {
        final Document doc = dr.findByName(docName);
        final List<Tag> tagList = tr.findByReference(doc.getId());

        model.put("docName", docName);
        model.put("document", doc);
        model.put("tag", Tag.createTagString(tagList));
        return "editor";
    }

    @RequestMapping(value = "/doc/create", method = RequestMethod.GET)
    public String viewEditor(DustModel model) {
        model.put("docName", "");
        model.put("document", new Document());
        return "editor";
    }

    @RequestMapping(value = "/doc/save", method = RequestMethod.POST)
    public String save(DocumentEditorForm form) throws UnsupportedEncodingException {
        Document doc = dr.findByName(form.getDocName());

        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final String user = (String) authentication.getPrincipal();

        if (doc != null) {
            // update
            doc.update(form.getContent(), DateTime.now().toDate(), user);

            ds.updateDocument(doc, form.getTag());
        } else {
            //create
            Document createDoc = Document.create(form.getDocName(), form.getContent(), user);

            ds.createDocument(createDoc, form.getTag());
        }
        return "redirect:/doc/" + encoding(form.getDocName());
    }

    @RequestMapping(value = "/doc/{docName}", method = RequestMethod.GET)
    public String view(@PathVariable String docName, DustModel model) {
        final Document doc = dr.findByName(docName);
        final List<Tag> tag = tr.findByReference(doc.getId());

        model.put("docName", docName);
        model.put("document", doc);
        model.put("tag", tag);
        return "view";
    }

    private String encoding(String docName) throws UnsupportedEncodingException {
        return URLEncoder.encode(docName, "UTF-8");
    }
}
