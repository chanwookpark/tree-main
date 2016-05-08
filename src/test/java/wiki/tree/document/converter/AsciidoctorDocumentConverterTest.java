package wiki.tree.document.converter;

import org.junit.Test;
import wiki.tree.document.domain.Document;

/**
 * @author chanwook
 */
public class AsciidoctorDocumentConverterTest {

    @Test
    public void simple() throws Exception {
        final String content =
                "= 테스트 아티클\n" +
                        "박찬욱<mail@mail.com>\n" +
                        "== 타이틀1\n" +
                        "블라블라\n";
        final String converted = convert(content);

        System.out.println(">>>>출력 결과>>>>\n" + converted);
    }

    private String convert(String content) {
        return new AsciidoctorDocumentConverter(createDoc(content)).convert(true);
    }

    private Document createDoc(String content) {
        final Document doc = new Document();
        doc.setContent(content);
        return doc;
    }
}
