package wiki.tree.publish;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import wiki.tree.document.converter.AsciidoctorDocumentConverter;
import wiki.tree.document.domain.Document;
import wiki.tree.document.repository.DocumentMongoRepository;

import java.io.IOException;
import java.net.URI;

/**
 * @author chanwook
 */
@RestController
public class AwsPublishController {

    private final Logger logger = LoggerFactory.getLogger(AwsPublishController.class);

    @Autowired
    DocumentMongoRepository dr;

    @Autowired
    AwsS3Template s3Template;

    @RequestMapping(value = "/publish/aws/s3/{docName}", method = RequestMethod.GET)
    public ResponseEntity publishToS3(@PathVariable String docName) {
        Document doc = dr.findByName(docName);

        //TODO Document 정보로 문서 헤더 꾸미기.
        final String convertedHtml = new AsciidoctorDocumentConverter(doc).convert(true);
        try {
            final URI objectURI = s3Template.publish(doc.getName(), convertedHtml);
            //TODO 별도 서비스로 정리해 분리
            doc.setPublishURI(objectURI.toString());
            dr.save(doc);
        } catch (IOException e) {
            logger.error("Failed upload document!", e);
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity(HttpStatus.OK);
    }
}
