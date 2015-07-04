package wiki.tree.main.file;

import com.dropbox.core.DbxClient;
import com.dropbox.core.DbxEntry;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxWriteMode;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author chanwook
 */
@Service
public class DropboxFileService implements FileService {

    private final Logger logger = LoggerFactory.getLogger(DropboxFileService.class);

    @Autowired
    private FileRepository fr;

    @Override
    public void saveFile(String docId, DbxClient dbxClient, MultipartFile fileSource) throws IOException, DbxException {

        final File file = createFileMeta(docId, fileSource);

        final DbxEntry.File uploadFile =
                dbxClient.uploadFile(file.uploadPath(), DbxWriteMode.add(), file.getSize(), fileSource.getInputStream());

        file.setUrl(uploadFile.path);

        fr.save(file);

        if (logger.isDebugEnabled()) {
            logger.debug("Upload file(Meta): " + file);
            logger.debug("Upload file(Dropbox): " + uploadFile);
        }
    }

    private File createFileMeta(String docId, MultipartFile fileSource) {
        File f = new File();
        f.setReference(docId);
        f.setFormat(fileSource.getContentType());
        f.setName(fileSource.getName());
        f.setOriginalFileName(fileSource.getOriginalFilename());
        f.setSize(fileSource.getSize());
        f.setUpdated(DateTime.now().toDate());

        return f;
    }
}
