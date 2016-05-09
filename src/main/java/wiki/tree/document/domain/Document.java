package wiki.tree.document.domain;

import org.joda.time.DateTime;
import org.springframework.data.annotation.Id;
import org.springframework.format.annotation.DateTimeFormat;

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

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME, pattern = "yyyy-MM-dd hh:mm")
    private Date created;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME, pattern = "yyyy-MM-dd hh:mm")
    private Date updated;

    private long revision;

    private String updateUser;

    //TODO 별도 Entity로 분리
    private String publishURI;

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

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public String getPublishURI() {
        return publishURI;
    }

    public void setPublishURI(String publishURI) {
        this.publishURI = publishURI;
    }

    public void update(String updateContent, Date updateTime, String updateUser) {
        this.content = updateContent;
        this.updated = updateTime;
        this.updateUser = updateUser;
    }

    public static Document create(String docName, String content, String user) {
        final Date now = DateTime.now().toDate();
        final Document d = new Document(docName, content, now, now, 1);
        d.setUpdateUser(user);
        return d;
    }
}
