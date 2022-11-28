package com.company.model;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TemplatePropertyParameters {
    public static final String SERIALIZED_NAME_DEFAULT = "default";
    @SerializedName("default")
    private String _default;

    public TemplatePropertyParameters _default(String _default) {
        this._default = _default;
        return this;
    }
    public String getDefault() {
        return this._default;
    }
    public void setDefault(String _default) {
        this._default = _default;
    }
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            TemplatePropertyParameters templatePropertyParameters = (TemplatePropertyParameters)o;
            return Objects.equals(this._default, templatePropertyParameters._default);
        } else {
            return false;
        }
    }
    public int hashCode() {
        return Objects.hash(new Object[]{this._default});
    }
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class TemplatePropertyParameters {\n");
        sb.append("    _default: ").append(this.toIndentedString(this._default)).append("\n");
        sb.append("}");
        return sb.toString();
    }
    private String toIndentedString(Object o) {
        return o == null ? "null" : o.toString().replace("\n", "\n    ");
    }
}
