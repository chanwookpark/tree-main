package wiki.tree.main.service;

import wiki.tree.main.domain.Document;

/**
 * @author chanwook
 */
public interface DocumentService {
    void updateDocument(Document document, String tagString);

    void createDocument(Document createDoc, String tagString);

    void saveTag(Document doc, String tagString);
}
