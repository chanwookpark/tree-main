package wiki.tree.main.document.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import wiki.tree.main.document.domain.Document;

/**
 * @author chanwook
 */
public interface DocumentRepository extends MongoRepository<Document, String> {
    Document findByName(String docName);
}
