package org.team4u.sql.domain;

/**
 * sql资源
 *
 * @author jay.wu
 */
public class SqlResource {

    private final String id;
    private final String content;

    public SqlResource(String id, String content) {
        this.id = id;
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public String getContent() {
        return content;
    }
}