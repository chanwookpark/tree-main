package wiki.tree.publish;

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

    ResourceLoader resourceLoader;

    public AwsS3Template(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public void publish(Document doc) throws IOException {
        final Resource resource = resourceLoader.getResource(getUploadPath(doc));
        final WritableResource writableResource = (WritableResource) resource;
        try (OutputStream outputStream = writableResource.getOutputStream()) {
            outputStream.write(doc.getContent().getBytes());
        }
    }

    private String getUploadPath(Document doc) {
        return "s3://treewiki/" + doc.getName();
    }

    public ResourceLoader getResourceLoader() {
        return resourceLoader;
    }
}
