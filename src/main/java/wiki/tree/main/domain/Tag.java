package wiki.tree.main.domain;

import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author chanwook
 */
@org.springframework.data.mongodb.core.mapping.Document
public class Tag {

    @Id
    private String id;

    private String value;

    private String reference;

    public Tag() {
    }

    public Tag(String tagId) {
        this.id = tagId;
    }

    public Tag(String value, String reference) {
        this.value = value;
        this.reference = reference;
    }


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

    public static String createTagString(List<Tag> tagList) {
        StringBuilder sb = new StringBuilder();
        for (Tag tag : tagList) {
            sb.append(tag.getValue());
            sb.append(" ");
        }
        return sb.toString();
    }

    public static List<Tag> createTag(String tagString, String docId) {
        final List<String> tagStringList = createTagList(tagString);
        List<Tag> tagList = new ArrayList<Tag>(tagStringList.size());
        for (String s : tagStringList) {
            tagList.add(new Tag(s, docId));
        }
        return tagList;
    }

    public static List<Tag> createTag(Map<String, String> tagMap) {
        List<Tag> list = new ArrayList<Tag>();
        for (String tagId : tagMap.values()) {
            list.add(new Tag(tagId));
        }
        return list;
    }

    public static Map<String, String> createTagMap(List<Tag> tagList) {
        Map<String, String> map = new ConcurrentHashMap<String, String>();
        for (Tag tag : tagList) {
            map.put(tag.getValue(), tag.getId());
        }
        return map;
    }

    public static List<String> createTagList(String tag) {
        final String[] tagArray = tag.split(" ");
        List<String> list = new ArrayList<String>(tagArray.length);
        for (String s : tagArray) {
            list.add(s);
        }
        return list;
    }
}
