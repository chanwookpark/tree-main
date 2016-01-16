package wiki.tree.file;

import com.dropbox.core.DbxClient;
import com.dropbox.core.DbxException;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import java.io.IOException;

/**
 * @author chanwook
 */
public interface FileService {

    void saveFile(String docId, DbxClient dbxClient, MultipartFile fileSource) throws IOException, DbxException;

    void getFile(DbxClient dbxClient, File file, ServletOutputStream outputStream) throws IOException, DbxException;
}
