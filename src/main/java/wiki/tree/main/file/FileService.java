package wiki.tree.main.file;

import com.dropbox.core.DbxClient;
import com.dropbox.core.DbxException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author chanwook
 */
public interface FileService {
    void saveFile(String docId, DbxClient dbxClient, MultipartFile fileSource) throws IOException, DbxException;
}
