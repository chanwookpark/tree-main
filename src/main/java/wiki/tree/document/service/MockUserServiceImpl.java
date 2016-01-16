package wiki.tree.document.service;

import org.springframework.stereotype.Service;
import wiki.tree.document.domain.Document;

import java.util.List;

/**
 * @author chanwook
 */
@Service
public class MockUserServiceImpl implements UserService {
    @Override
    public void replaceUserIdToName(List<Document> documentList) {
        for (Document doc : documentList) {
            if ("107293774320992191474".equals(doc.getUpdateUser())) {
                doc.setUpdateUser("박찬욱");
            }
        }
    }
}
