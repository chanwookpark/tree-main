package wiki.tree.main.domain;

import org.springframework.data.annotation.Id;

import java.util.Date;

/**
 * @author chanwook
 */
@org.springframework.data.mongodb.core.mapping.Document
public class Document {

    @Id
    private String id;

    private String name;

    private String content;

    private Date created;

    private Date updated;

    private long revision;

    public Document() {
    }

    public Document(String name, String content, Date created, Date updated, long revision) {
        this.name = name;
        this.content = content;
        this.created = created;
        this.updated = updated;
        this.revision = revision;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public long getRevision() {
        return revision;
    }

    public void setRevision(long revision) {
        this.revision = revision;
    }

    public void update(String updateContent, Date updateTime) {
        this.content = updateContent;
        this.updated = updateTime;
    }
}
