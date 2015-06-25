package wiki.tree.main.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import wiki.tree.main.domain.Document;

/**
 * @author chanwook
 */
public interface DocumentRepository extends MongoRepository<Document, String> {
    Document findByName(String docName);
}
