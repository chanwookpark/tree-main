package wiki.tree.document.service;

import wiki.tree.document.domain.Document;

/**
 * @author chanwook
 */
public interface DocumentService {
    void updateDocument(Document document, String tagString);

    void createDocument(Document createDoc, String tagString);

    void saveTag(Document doc, String tagString);
}
