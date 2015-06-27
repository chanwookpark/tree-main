package wiki.tree.main.web;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import r2.dustjs.spring.DustModel;
import wiki.tree.main.domain.Document;
import wiki.tree.main.domain.Tag;
import wiki.tree.main.repository.DocumentRepository;
import wiki.tree.main.repository.TagRepository;
import wiki.tree.main.service.DocumentService;
import wiki.tree.main.web.dto.DocumentEditorForm;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
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
    private DocumentService service;

    @RequestMapping(value = "/doc/{docName}/edit", method = RequestMethod.GET)
    public String viewEditor(@PathVariable String docName, DustModel model) {
        final Document doc = dr.findByName(docName);
        final List<Tag> tagList = tr.findByReference(doc.getId());

        model.put("docName", docName);
        model.put("document", doc);
        model.put("tag", Tag.createTagString(tagList));
        return "editor";
    }

    @RequestMapping(value = "/doc/save", method = RequestMethod.POST)
    public String save(DocumentEditorForm form) throws UnsupportedEncodingException {
        final Document doc = dr.findByName(form.getDocName());
        if (doc != null) {
            // update
            //TODO 이건 세션처리를 어떻게 할까나?
            doc.update(form.getContent(), DateTime.now().toDate(), "박찬욱");

            service.updateDocument(doc, form.getTag());
        } else {
            //TODO tx 처리
            //create
            Document createDoc = createDocument(form);

            service.createDocument(createDoc, form.getTag());
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

    private Document createDocument(DocumentEditorForm form) {
        final Date now = DateTime.now().toDate();
        final Document d = new Document(form.getDocName(), form.getContent(), now, now, 1);
        d.setUpdateUser("박찬욱");
        return d;
    }

}
