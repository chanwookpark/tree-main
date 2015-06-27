package wiki.tree.main.domain;

import org.springframework.data.annotation.Id;

/**
 * @author chanwook
 */
@org.springframework.data.mongodb.core.mapping.Document
public class Tag {

    @Id
    private String id;

    private String value;

    private String reference;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }
}
