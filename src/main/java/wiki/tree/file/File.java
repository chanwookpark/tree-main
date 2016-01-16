package wiki.tree.file;

import org.springframework.data.annotation.Id;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author chanwook
 */
@org.springframework.data.mongodb.core.mapping.Document
public class File {

    @Id
    private String id;

    private String name;

    private String originalFileName;

    private String url;

    private String format;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME, pattern = "yyyy-MM-dd hh:mm")
    private Date updated;

    private long size;

    private String reference;
    private String revision;


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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public void setOriginalFileName(String originalName) {
        this.originalFileName = originalName;
    }

    public String getOriginalFileName() {
        return originalFileName;
    }

    public void setRevision(String revision) {
        this.revision = revision;
    }

    public String getRevision() {
        return revision;
    }

    @Override
    public String toString() {
        return "File{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", originalFileName='" + originalFileName + '\'' +
                ", url='" + url + '\'' +
                ", format='" + format + '\'' +
                ", updated=" + updated +
                ", size=" + size +
                ", reference='" + reference + '\'' +
                ", revision='" + revision + '\'' +
                '}';
    }

    public String uploadPath() {
        return "/" + getReference() + "/" + getOriginalFileName();
    }
}
