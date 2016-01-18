package wiki.tree.document.converter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import wiki.tree.config.AwsConfig;
import wiki.tree.document.domain.Document;
import wiki.tree.publish.AwsS3Template;

/**
 * @author chanwook
 */
@RunWith(value = SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AwsConfig.class)
public class AwsS3TemplateTest {

    @Autowired
    AwsS3Template template;

    @Autowired
    Environment env;

    @Test
    public void uploadPage() throws Exception {

        final Document doc = new Document();
        doc.setName("test-문서" + System.nanoTime());
        doc.setContent("Hello~!!");

        template.publish(doc);

    }
}
