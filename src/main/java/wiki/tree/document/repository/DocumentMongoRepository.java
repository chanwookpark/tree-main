package wiki.tree.document.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import wiki.tree.document.domain.Document;

/**
 * @author chanwook
 */
public interface DocumentMongoRepository extends MongoRepository<Document, String> {
    Document findByName(String docName);
}
