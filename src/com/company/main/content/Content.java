package com.company.main.content;

/**
 * Created by alex6 on 05.06.2017.
 */
public class Content {
    ContentType contentType;
    String assistField; //label in sentence etc.
    String mnem;
    LexemeOperand op1;
    LexemeOperand op2;

    public Content(ContentType contentType, String assistField, String mnem, LexemeOperand op1, LexemeOperand op2) {
        this.contentType = contentType;
        this.assistField = assistField;
        this.mnem = mnem;
        this.op1 = op1;
        this.op2 = op2;
    }

    public Content() {
    }

    public ContentType getContentType() {
        return contentType;
    }

    public void setContentType(ContentType contentType) {
        this.contentType = contentType;
    }

    public String getAssistField() {
        return assistField;
    }

    public void setAssistField(String assistField) {
        this.assistField = assistField;
    }

    public String getMnem() {
        return mnem;
    }

    public void setMnem(String mnem) {
        this.mnem = mnem;
    }

    public LexemeOperand getOp1() {
        return op1;
    }

    public void setOp1(LexemeOperand op1) {
        this.op1 = op1;
    }

    public LexemeOperand getOp2() {
        return op2;
    }

    public void setOp2(LexemeOperand op2) {
        this.op2 = op2;
    }

    @Override
    public String toString() {
        return "Content{" +
                "contentType=" + contentType +
                ", assistField='" + assistField + '\'' +
                ", mnem='" + mnem + '\'' +
                ", op1=" + op1 +
                ", op2=" + op2 +
                '}';
    }
}
