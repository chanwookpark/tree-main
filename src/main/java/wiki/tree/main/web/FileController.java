package wiki.tree.main.web;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import r2.dustjs.spring.DustModel;
import wiki.tree.main.domain.File;
import wiki.tree.main.repository.FileRepository;

import java.util.List;

/**
 * @author chanwook
 */
@Controller
public class FileController {

    private final Logger logger = LoggerFactory.getLogger(FileController.class);

    @Autowired
    private FileRepository fr;

    @RequestMapping(value = "/doc/{docId}/file", method = RequestMethod.GET)
    public String view(@PathVariable String docId, DustModel model) {
        List<File> fileList = fr.findByReference(docId);

        model.put("docId", docId);
        model.put("fileList", fileList);

        return "/fileUpload";
    }

    @RequestMapping(value = "/doc/{docId}/file", method = RequestMethod.POST)
    public String uploadFile(@PathVariable String docId, @RequestPart("uploadFile") MultipartFile fileSource) {

        File file = createFileMeta(docId, fileSource);
        fr.save(file);

        return "redirect:/doc/" + docId + "/file";
    }

    private File createFileMeta(String docId, MultipartFile fileSource) {
        File f = new File();
        f.setReference(docId);
        f.setFormat(fileSource.getContentType());
        f.setName(fileSource.getName());
        f.setOriginalFileName(fileSource.getOriginalFilename());
        f.setSize(fileSource.getSize());
        f.setUpdated(DateTime.now().toDate());
        //TODO Dropbox 붙이고 추가하기
        f.setUrl("#");
        if (logger.isDebugEnabled()) {
            logger.debug("Create file: " + f);
        }
        return f;
    }
}
