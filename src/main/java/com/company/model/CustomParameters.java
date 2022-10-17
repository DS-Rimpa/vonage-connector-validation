package com.company.model;

import java.util.List;

public class CustomParameters {
    List<TemplatePropertyParameters> body;
    List<TemplatePropertyParameters> header;
    List<TemplatePropertyParameters> footer;
    List<TemplatePropertyParameters> button;
    public CustomParameters() {
    }

    public List<TemplatePropertyParameters> getBody() {
        return this.body;
    }

    public void setBody(List<TemplatePropertyParameters> body) {
        this.body = body;
    }

    public List<TemplatePropertyParameters> getHeader() {
        return this.header;
    }

    public void setHeader(List<TemplatePropertyParameters> header) {
        this.header = header;
    }

    public List<TemplatePropertyParameters> getFooter() {
        return this.footer;
    }

    public void setFooter(List<TemplatePropertyParameters> footer) {
        this.footer = footer;
    }

    public List<TemplatePropertyParameters> getButton() {
        return this.button;
    }

    public void setButton(List<TemplatePropertyParameters> button) {
        this.button = button;
    }
}