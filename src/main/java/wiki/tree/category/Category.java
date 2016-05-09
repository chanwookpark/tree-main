package wiki.tree.category;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * @author chanwook
 */
@Entity
@Data
@Table(name = "CATEGORY_M")
public class Category {

    @Id
    @GeneratedValue
    private long id;

    @Column(length = 50, nullable = false)
    private String name;

    @Column(length = 200)
    private String description;

    @Version
    @Temporal(TemporalType.TIMESTAMP)
    private Date updated;

}
