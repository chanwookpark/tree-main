package wiki.tree.main.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import wiki.tree.main.domain.File;

import java.util.List;

/**
 * @author chanwook
 */
public interface FileRepository extends MongoRepository<File, String> {

    List<File> findByReference(String docId);

}
