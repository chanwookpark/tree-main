package wiki.tree.document.converter;

import org.junit.Test;
import wiki.tree.document.domain.Document;

/**
 * @author chanwook
 */
public class AsciidoctorDocumentConverterTest {

    @Test
    public void simple() throws Exception {
//        convert("== title1 \n*title* is bold.\n == title 2\nHow?");
        String text = "=Hello, AsciiDoc!\nDoc Writer <doc@example.com>\nAn introduction to http://asciidoc.org[AsciiDoc].\n"
                + "== First Section\n* item 1\n* item 2";
        convert(text);
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
