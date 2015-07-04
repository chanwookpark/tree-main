package wiki.tree.main.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import r2.dustjs.spring.DustModel;

/**
 * @author chanwook
 */
@Controller
public class FileController {

    private final Logger logger = LoggerFactory.getLogger(FileController.class);

    @RequestMapping(value = "/doc/{docId}/file", method = RequestMethod.GET)
    public String view(@PathVariable String docId, DustModel model) {
        model.put("docId", docId);

        return "/fileUpload";
    }

    @RequestMapping(value = "/doc/{docId}/file", method = RequestMethod.POST)
    public String uploadFile(@PathVariable String docId, @RequestPart("uploadFile") MultipartFile file) {

        if (logger.isDebugEnabled()) {
            logger.debug("Uploaded file: " + file);
        }
        return "redirect:/doc/" + docId + "/file";
    }
}
