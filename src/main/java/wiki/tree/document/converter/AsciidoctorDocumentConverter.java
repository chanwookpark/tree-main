package wiki.tree.document.converter;

import org.asciidoctor.Asciidoctor;
import org.asciidoctor.Options;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wiki.tree.document.domain.Document;

/**
 * @author chanwook
 */
public class AsciidoctorDocumentConverter implements DocumentConverter {

    private final Logger logger = LoggerFactory.getLogger(AsciidoctorDocumentConverter.class);

    private final Document doc;

    public AsciidoctorDocumentConverter(Document doc) {
        this.doc = doc;
    }

    @Override
    public String convert() {
        // 기본은 헤더를 모두 가지고 오는 것으로...
        return convert(true);
    }

    /**
     * <pre>
     * true로 주면 <html></html>로 시작하는 HTML 전체 페이지를 반환한다.
     * </pre>
     *
     * @param containsHeaderFooter
     * @return
     */
    @Override
    public String convert(boolean containsHeaderFooter) {
        final Asciidoctor asciidoctor = Asciidoctor.Factory.create();

        Options options = new Options();
        options.setHeaderFooter(containsHeaderFooter);

        final String html = asciidoctor.convert(doc.getContent(), options);

        if (logger.isDebugEnabled()) {
            logger.debug("[Source HTML]\n" + doc.getContent().trim() + "\n[Converted HTML]\n" + html.trim());
        }

        return html;
    }
}
