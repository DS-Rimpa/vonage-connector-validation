package com.company.model;

import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@NoArgsConstructor
@Data
@Builder
@AllArgsConstructor
public class TemplateProperty {
    private List<TemplatePropertyParameters> parameters = new ArrayList();

    public TemplateProperty parameters(List<TemplatePropertyParameters> parameters) {
        this.parameters = parameters;
        return this;
    }
    public TemplateProperty addParametersItem(TemplatePropertyParameters parametersItem) {
        if (this.parameters == null) {
            this.parameters = new ArrayList();
        }
        this.parameters.add(parametersItem);
        return this;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            TemplateProperty templateProperty = (TemplateProperty)o;
            return Objects.equals(this.parameters, templateProperty.parameters);
        } else {
            return false;
        }
    }
    public int hashCode() {
        return Objects.hash(new Object[]{this.parameters});
    }
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class TemplateProperty {\n");
        sb.append("    parameters: ").append(this.toIndentedString(this.parameters)).append("\n");
        sb.append("}");
        return sb.toString();
    }
    private String toIndentedString(Object o) {
        return o == null ? "null" : o.toString().replace("\n", "\n    ");
    }
}
