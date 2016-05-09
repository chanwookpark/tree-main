package wiki.tree.document.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import wiki.tree.document.domain.Tag;

import java.util.List;

/**
 * @author chanwook
 */
public interface TagMongoRepository extends MongoRepository<Tag, String> {
    List<Tag> findByReference(String id);
}
