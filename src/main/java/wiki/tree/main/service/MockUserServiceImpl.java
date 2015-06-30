package wiki.tree.main.service;

import org.springframework.stereotype.Service;
import wiki.tree.main.domain.Document;

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
