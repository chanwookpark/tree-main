package wiki.tree.main.web;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import r2.dustjs.spring.DustModel;
import wiki.tree.main.domain.Document;
import wiki.tree.main.domain.Tag;
import wiki.tree.main.repository.DocumentRepository;
import wiki.tree.main.repository.TagRepository;
import wiki.tree.main.web.dto.DocumentEditorForm;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author chanwook
 */
@Controller
public class DocumentController {

    @Autowired
    private DocumentRepository dr;

    @Autowired
    private TagRepository tr;

    @RequestMapping(value = "/doc/{docName}/edit", method = RequestMethod.GET)
    public String viewEditor(@PathVariable String docName, DustModel model) {
        final Document doc = dr.findByName(docName);
        final List<Tag> tagList = tr.findByReference(doc.getId());

        model.put("docName", docName);
        model.put("document", doc);
        model.put("tag", createTagString(tagList));
        return "editor";
    }

    private String createTagString(List<Tag> tagList) {
        StringBuilder sb = new StringBuilder();
        for (Tag tag : tagList) {
            sb.append(tag.getValue());
            sb.append(" ");
        }
        return sb.toString();
    }

    @RequestMapping(value = "/doc/save", method = RequestMethod.POST)
    public String save(DocumentEditorForm form) throws UnsupportedEncodingException {
        final Document doc = dr.findByName(form.getDocName());
        if (doc != null) {
            // update
            //TODO 이건 세션처리를 어떻게 할까나?
            doc.update(form.getContent(), DateTime.now().toDate(), "박찬욱");

            dr.save(doc);

            final List<Tag> savedTag = tr.findByReference(doc.getId());
            Map<String, String> tagMap = createTagMap(savedTag);
            List<String> updateTag = createUpdateTag(form.getTag());
            List<Tag> createTag = new ArrayList<Tag>();
            for (String tag : updateTag) {
                if (tagMap.containsKey(tag)) {
                    tagMap.remove(tag);
                } else {
                    createTag.add(new Tag(tag, doc.getId()));
                }
            }

            // 신규 Tag는 저장
            tr.save(createTag);

            // 남은 Tag는 삭제
            tr.delete(createTag(tagMap));

        } else {
            //TODO tx 처리
            //create
            final Document createDoc = createDocument(form);
            Document saved = dr.save(createDoc);
            form.setDocId(saved.getId());
            final Tag tag = createTag(form);
            tr.save(tag);
        }
        return "redirect:/doc/" + encoding(form.getDocName());
    }

    private List<Tag> createTag(Map<String, String> tagMap) {
        List<Tag> list = new ArrayList<Tag>();
        for (String tagId : tagMap.values()) {
            list.add(new Tag(tagId));
        }
        return list;
    }

    private Map<String, String> createTagMap(List<Tag> tagList) {
        Map<String, String> map = new ConcurrentHashMap<String, String>();
        for (Tag tag : tagList) {
            map.put(tag.getValue(), tag.getId());
        }
        return map;
    }

    private List<String> createUpdateTag(String tag) {
        final String[] tagArray = tag.split(" ");
        return CollectionUtils.arrayToList(tagArray);
    }

    private Tag createTag(DocumentEditorForm form) {
        Tag tag = new Tag();
        tag.setValue(form.getTag());
        tag.setReference(form.getDocId());
        return tag;
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
