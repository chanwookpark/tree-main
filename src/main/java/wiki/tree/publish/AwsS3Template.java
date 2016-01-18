package wiki.tree.publish;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.WritableResource;
import wiki.tree.document.domain.Document;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author chanwook
 */
public class AwsS3Template {

    private final Logger logger = LoggerFactory.getLogger(AwsS3Template.class);

    ResourceLoader resourceLoader;

    public AwsS3Template(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    //TODO 1. 컨텐츠 타입 지정 2. Make public
    public void publish(Document doc) throws IOException {
        final String uploadPath = getUploadPath(doc);

        final Resource resource = resourceLoader.getResource(uploadPath);
        final WritableResource writableResource = (WritableResource) resource;
        try (OutputStream outputStream = writableResource.getOutputStream()) {

            if (logger.isDebugEnabled()) {
                logger.debug("Upload Document to AWS S3 '" + uploadPath + "'+\tcontent(maybe long..): " + doc.getContent());
            }

            outputStream.write(doc.getContent().getBytes());
        }
    }

    private String getUploadPath(Document doc) {
        return "s3://treewiki/" + doc.getName() + ".html";
    }

    public ResourceLoader getResourceLoader() {
        return resourceLoader;
    }
}
