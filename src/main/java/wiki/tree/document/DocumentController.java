package wiki.tree.document;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import wiki.tree.document.domain.Document;
import wiki.tree.document.domain.Tag;
import wiki.tree.document.repository.DocumentRepository;
import wiki.tree.document.repository.TagRepository;
import wiki.tree.document.service.DocumentService;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

/**
 * @author chanwook
 */
@Controller
public class DocumentController {

    private final Logger logger = LoggerFactory.getLogger(DocumentController.class);

    @Autowired
    private DocumentRepository dr;

    @Autowired
    private TagRepository tr;

    @Autowired
    private DocumentService ds;

    @RequestMapping(value = "/doc/edit/{docName}", method = RequestMethod.GET)
    public String viewEditor(@PathVariable String docName, ModelMap model) {
        final Document doc = dr.findByName(docName);

        if (!isEditableUser(doc)) {
            throw new RuntimeException("Not permitted update to document!");
        }

        final List<Tag> tagList = tr.findByReference(doc.getId());

        model.put("docName", docName);
        model.put("document", doc);
        model.put("tag", Tag.createTagString(tagList));
        return "editor";
    }

    private boolean isEditableUser(Document doc) {
        final String userName = getUserName();
        if (StringUtils.hasText(userName)
                && StringUtils.hasText(doc.getUpdateUser())
                && userName.equals(doc.getUpdateUser())) {
            return true;
        }
        return false;
    }

    @RequestMapping(value = "/doc/create", method = RequestMethod.GET)
    public String viewEditor(ModelMap model) {
        model.put("docName", "");
        model.put("document", new Document());
        return "editor";
    }

    @RequestMapping(value = "/doc/save", method = RequestMethod.POST)
    public String save(DocumentEditorForm form) throws UnsupportedEncodingException {
        Document doc = dr.findByName(form.getDocName());
        final String user = getUserName();

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

    private String getUserName() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (String) authentication.getPrincipal();
    }

    private String encoding(String docName) throws UnsupportedEncodingException {
        return URLEncoder.encode(docName, "UTF-8");
    }
}
