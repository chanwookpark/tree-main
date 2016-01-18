package wiki.tree.document.converter;

/**
 * @author chanwook
 */
public interface DocumentConverter {

    String convert();

    String convert(boolean containsHeaderFooter);
}
