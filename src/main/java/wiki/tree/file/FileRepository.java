package wiki.tree.file;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * @author chanwook
 */
public interface FileRepository extends MongoRepository<File, String> {

    List<File> findByReference(String docId);

}
