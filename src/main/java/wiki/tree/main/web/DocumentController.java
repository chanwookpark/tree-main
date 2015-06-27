package wiki.tree.main.web;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import r2.dustjs.spring.DustModel;
import wiki.tree.main.domain.Document;
import wiki.tree.main.repository.DocumentRepository;
import wiki.tree.main.web.dto.DocumentEditorForm;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;

/**
 * @author chanwook
 */
@Controller
public class DocumentController {

    @Autowired
    private DocumentRepository r;

    @RequestMapping(value = "/doc/{docName}/edit", method = RequestMethod.GET)
    public String viewEditor(@PathVariable String docName, DustModel model) {
        model.put("docName", docName);
        model.put("document", r.findByName(docName));
        return "editor";
    }

    @RequestMapping(value = "/doc/save", method = RequestMethod.POST)
    public String save(DocumentEditorForm form) throws UnsupportedEncodingException {
        final Document doc = r.findByName(form.getDocName());
        if (doc != null) {
            // update
            //TODO 이건 세션처리를 어떻게 할까나?
            doc.update(form.getContent(), DateTime.now().toDate(), "박찬욱");

            r.save(doc);
        } else {
            //create
            final Document createDoc = createDocument(form);
            r.save(createDoc);
        }
        return "redirect:/doc/" + encoding(form.getDocName());
    }

    @RequestMapping(value = "/doc/create", method = RequestMethod.GET)
    public String viewEditor(DustModel model) {
        model.put("docName", "");
        model.put("document", new Document());
        return "editor";
    }

    @RequestMapping(value = "/doc/{docName}", method = RequestMethod.GET)
    public String view(@PathVariable String docName, DustModel model) {
        model.put("docName", docName);
        model.put("document", r.findByName(docName));
        return "view";
    }

    private String encoding(String docName) throws UnsupportedEncodingException {
        return URLEncoder.encode(docName, "UTF-8");
    }

    private Document createDocument(DocumentEditorForm form) {
        final Date now = DateTime.now().toDate();
        final Document d = new Document(form.getDocName(), form.getContent(), now, now, 1);
        d.setUpdateUser("박찬욱");
        return d;
    }

}
