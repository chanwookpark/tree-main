package wiki.tree.main.document.service;

import wiki.tree.main.document.domain.Document;

import java.util.List;

/**
 * @author chanwook
 */
public interface UserService {
    void replaceUserIdToName(List<Document> documentList);
}
