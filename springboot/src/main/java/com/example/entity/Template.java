package com.example.entity;

public class Template {
    private Integer templateListId;
    private String name;
    private String preview;
    private String style;

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public Integer getTemplateListId() {
        return templateListId;
    }

    public void setTemplateListId(Integer templateListId) {
        this.templateListId = templateListId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPreview() {
        return preview;
    }

    public void setPreview(String preview) {
        this.preview = preview;
    }
}
