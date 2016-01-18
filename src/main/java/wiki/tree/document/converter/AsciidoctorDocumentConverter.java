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
        return convert(false);
    }

    @Override
    public String convert(boolean containsHeaderFooter) {
        final Asciidoctor asciidoctor = Asciidoctor.Factory.create();

        Options options = new Options();
        options.setHeaderFooter(containsHeaderFooter);

        final String html = asciidoctor.convert(doc.getContent(), options);

        if (logger.isDebugEnabled()) {
            logger.debug("\n>> Source HTML\n" + doc.getContent() + "\n>>Converted HTML\n" + html);
        }

        return html;
    }
}
