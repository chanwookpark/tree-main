package wiki.tree.document;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import wiki.tree.document.converter.AsciidoctorDocumentConverter;
import wiki.tree.document.domain.Document;
import wiki.tree.document.domain.Tag;
import wiki.tree.document.repository.DocumentMongoRepository;
import wiki.tree.document.repository.TagMongoRepository;
import wiki.tree.document.service.UserService;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

/**
 * @author chanwook
 */
@Controller
public class PostViewController {

    private final Logger logger = LoggerFactory.getLogger(PostViewController.class);

    @Autowired
    private DocumentMongoRepository dr;

    @Autowired
    private TagMongoRepository tr;

    @Autowired
    private UserService us;

    @RequestMapping("/change")
    public String post(Pageable pageRequest, ModelMap model) {
        final Page<Document> page = dr.findAll(pageRequest);
        //TODO 새로짜기....
        us.replaceUserIdToName(page.getContent());
        model.put("postList", page.getContent());
        return "post";
    }

    @RequestMapping(value = "/doc/view/{name}", method = RequestMethod.GET)
    public String view(@PathVariable String name, ModelMap model) throws UnsupportedEncodingException {
        String docName = URLDecoder.decode(name, "UTF-8");
        final Document doc = dr.findByName(docName);

        model.put("name", docName);
        model.put("document", doc);
        model.put("_html", doc.getContent());

        try {
            final List<Tag> tag = tr.findByReference(doc.getId());
            model.put("tag", tag);
        } catch (Exception e) {
            logger.error("Tag 조회 중 에러 발생. 태그를 노출하지 않습니다.", e);
        }

        return "view";
    }

    @RequestMapping(value = "/doc/preview/{name}")
    @ResponseBody
    public ResponseEntity preview(@PathVariable String name) {
        final Document doc = dr.findByName(name);
        String html = new AsciidoctorDocumentConverter(doc).convert();
        //TODO add tag...

        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_HTML);
        return new ResponseEntity(html, headers, HttpStatus.OK);
    }
}
