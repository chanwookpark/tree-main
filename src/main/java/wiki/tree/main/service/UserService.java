package wiki.tree.main.service;

import wiki.tree.main.domain.Document;

import java.util.List;

/**
 * @author chanwook
 */
public interface UserService {
    void replaceUserIdToName(List<Document> documentList);
}
