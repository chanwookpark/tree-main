package wiki.tree.document.service;

import wiki.tree.document.domain.Document;

import java.util.List;

/**
 * @author chanwook
 */
public interface UserService {
    void replaceUserIdToName(List<Document> documentList);
}
