package com.company.model;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessagePropertyContent {
    private MessagePropertyContent.TypeEnum type;
    private String text;
    private TemplateProperty template = null;
    private CustomProperty custom = null;


    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            MessagePropertyContent messagePropertyContent = (MessagePropertyContent)o;
            return Objects.equals(this.type, messagePropertyContent.type)
                    && Objects.equals(this.text, messagePropertyContent.text)
                    && Objects.equals(this.template, messagePropertyContent.template)
                    && Objects.equals(this.custom, messagePropertyContent.custom);
        } else {
            return false;
        }
    }
    public int hashCode() {
        return Objects.hash(new Object[]{this.type, this.text, this.template});
    }
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class MessagePropertyContent {\n");
        sb.append("    type: ").append(this.toIndentedString(this.type)).append("\n");
        sb.append("    text: ").append(this.toIndentedString(this.text)).append("\n");
        sb.append("    template: ").append(this.toIndentedString(this.template)).append("\n");
        sb.append("    custom: ").append(this.toIndentedString(this.custom)).append("\n");
        sb.append("}");
        return sb.toString();
    }
    private String toIndentedString(Object o) {
        return o == null ? "null" : o.toString().replace("\n", "\n    ");
    }
    public static enum TypeEnum {
        text("text"),
        file("file"),
        template("template"),
        custom("custom");
        private String value;
        private TypeEnum(String value) {
            this.value = value;
        }
        public String getValue() {
            return this.value;
        }
        public String toString() {
            return String.valueOf(this.value);
        }
        public static MessagePropertyContent.TypeEnum fromValue(String value) {
            MessagePropertyContent.TypeEnum[] var1 = values();
            int var2 = var1.length;
            for(int var3 = 0; var3 < var2; ++var3) {
                MessagePropertyContent.TypeEnum b = var1[var3];
                if (b.value.equals(value)) {
                    return b;
                }
            }
            throw new IllegalArgumentException("Unexpected value '" + value + "'");
        }
        public static class Adapter extends TypeAdapter<TypeEnum> {
            public Adapter() {
            }
            public void write(final JsonWriter jsonWriter, final MessagePropertyContent.TypeEnum enumeration) throws IOException {
                jsonWriter.value(enumeration.getValue());
            }
            public MessagePropertyContent.TypeEnum read(final JsonReader jsonReader) throws IOException {
                String value = jsonReader.nextString();
                return MessagePropertyContent.TypeEnum.fromValue(value);
            }
        }
    }
}