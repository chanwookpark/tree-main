package wiki.tree.main.file;

import com.dropbox.core.DbxClient;
import com.dropbox.core.DbxException;
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
import wiki.tree.main.security.dropbox.DropboxConstants;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

/**
 * @author chanwook
 */
@Controller
public class FileController {

    private final Logger logger = LoggerFactory.getLogger(FileController.class);

    @Autowired
    private FileRepository fr;

    @Autowired
    private FileService fs;

    @RequestMapping(value = "/doc/{docId}/file", method = RequestMethod.GET)
    public String view(@PathVariable String docId, DustModel model) {
        List<File> fileList = fr.findByReference(docId);

        model.put("docId", docId);
        model.put("fileList", fileList);

        return "/fileUpload";
    }

    @RequestMapping(value = "/doc/{docId}/file", method = RequestMethod.POST)
    public String uploadFile(@PathVariable String docId,
                             @RequestPart("uploadFile") MultipartFile fileSource,
                             HttpSession session) throws IOException, DbxException {

        Object apiObject = session.getAttribute(DropboxConstants.SESSION_CLIENT_API);
        if (apiObject == null) {
            // dropbox 로그인하러 가기
            return "redirect:/security/dropbox/connect";
        }
        DbxClient dbxClient = (DbxClient) apiObject;
        fs.saveFile(docId, dbxClient, fileSource);

        return "redirect:/doc/" + docId + "/file";
    }
}
