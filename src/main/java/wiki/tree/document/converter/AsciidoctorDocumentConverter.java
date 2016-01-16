package wiki.tree.document.converter;

import org.asciidoctor.Asciidoctor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wiki.tree.document.domain.Document;

import java.util.HashMap;

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
        final Asciidoctor asciidoctor = Asciidoctor.Factory.create();
        final HashMap<String, Object> attributes = new HashMap<>();
        attributes.put("doctype", "book");

        final HashMap<String, Object> options = new HashMap<>();
        options.put("attributes", attributes);

        final String html = asciidoctor.convert(doc.getContent(), options);

        if (logger.isDebugEnabled()) {
            logger.debug("\n[Source HTML]\n" + doc.getContent() + "\n[Converted HTML]\n" + html);
        }

        return html;
    }
}
