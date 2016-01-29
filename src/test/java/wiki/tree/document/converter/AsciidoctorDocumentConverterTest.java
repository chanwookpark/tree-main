package wiki.tree.document.converter;

import org.junit.Test;
import wiki.tree.document.domain.Document;

/**
 * @author chanwook
 */
public class AsciidoctorDocumentConverterTest {

    @Test
    public void simple() throws Exception {
        convert("== title1 \n*title* is bold.\n== title 2\nHow?");

        convert("= Article Title\nDoc Writer <mail@email.com>\nv1.0, 2016-01-30");

    }

    private String convert(String content) {
        return new AsciidoctorDocumentConverter(createDoc(content)).convert();
    }

    private Document createDoc(String content) {
        final Document doc = new Document();
        doc.setContent(content);
        return doc;
    }
}
