package wiki.tree.publish;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.Upload;
import com.amazonaws.services.s3.transfer.model.UploadResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

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
    public URI publish(String name, String content) throws IOException {
        TransferManager transferManager = new TransferManager(this.amazonS3);
        final ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType("text/html");
        metadata.setContentLength(content.length());
        metadata.setContentEncoding("UTF-8");

        if (logger.isDebugEnabled()) {
            logger.debug("Upload Document to AWS S3 treewiki:" + name + "'+\tcontent(maybe long..): " + content.trim());
        }

        final PutObjectRequest uploadRequest = new PutObjectRequest("treewiki", name + ".html", getInputStream(content), metadata);
        uploadRequest.withCannedAcl(CannedAccessControlList.PublicRead);
        final Upload upload = transferManager.upload(uploadRequest);

        try {
            final UploadResult uploadResult = upload.waitForUploadResult();

            // 업로드한 오브젝트의 URI를 반환
            final S3Object object = amazonS3.getObject(uploadRequest.getBucketName(), uploadResult.getKey());
            return object.getObjectContent().getHttpRequest().getURI();
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
