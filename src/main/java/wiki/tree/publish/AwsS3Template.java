package wiki.tree.publish;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.Upload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wiki.tree.document.domain.Document;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author chanwook
 */
public class AwsS3Template {

    private final Logger logger = LoggerFactory.getLogger(AwsS3Template.class);

    private final AmazonS3 amazonS3;

    public AwsS3Template(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
    }

    //TODO 1. Make public
    public void publish(Document doc) throws IOException {
        final String content = doc.getContent();

        TransferManager transferManager = new TransferManager(this.amazonS3);
        final ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType("text/html");
        metadata.setContentLength(content.length());

        if (logger.isDebugEnabled()) {
            logger.debug("Upload Document to AWS S3 treewiki:" + doc.getName() + "'+\tcontent(maybe long..): " + doc.getContent());
        }

        final Upload upload = transferManager.upload("treewiki", doc.getName() + ".html", getInputStream(content), metadata);
        try {
            upload.waitForUploadResult();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    private InputStream getInputStream(String doc) {
        return new ByteArrayInputStream(doc.getBytes());
    }

    public AmazonS3 getAmazonS3() {
        return amazonS3;
    }
}
