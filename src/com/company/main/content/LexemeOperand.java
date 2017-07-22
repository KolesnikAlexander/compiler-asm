package com.company.main.content;

/**
 * Created by alex6 on 05.06.2017.
 */
public class LexemeOperand {

    private boolean constant;
    private String constantValue;

    private boolean textConstant;
    private String textConstantValue;

    private boolean register8;
    private String register8Value;

    private boolean register32;
    private String register32Value;

    private  boolean directAddress;
    private String directAdressValue;

    private  boolean baseIndexMultiplier;

    private boolean reg1;
    private String reg1Value;

    private  boolean reg2;
    private  String reg2Value;

    private boolean multiplier;
    private  String multiplierValue;

    private boolean bytePtr;

    private boolean wordPtr;

    private  boolean dwordPtr;

    private boolean farPtr;

    private boolean segChange;
        String segChangeValue;

    @Override
    public String toString() {
        return "\n LexemeOperand{" +
                "\n constant=" + constant +
                "\n , constantValue='" + constantValue + '\'' +
                "\n textConstant=" + textConstant +
                "\n textConstantValue=" + textConstantValue +
                "\n , register8=" + register8 +
                "\n , register8Value='" + register8Value + '\'' +
                "\n , register32=" + register32 +
                "\n , register32Value='" + register32Value + '\'' +
                "\n , directAddress=" + directAddress +
                "\n , directAdressValue='" + directAdressValue + '\'' +
                "\n , baseIndexMultiplier=" + baseIndexMultiplier +
                "\n , reg1=" + reg1 +
                "\n , reg1Value='" + reg1Value + '\'' +
                "\n , reg2=" + reg2 +
                "\n , reg2Value='" + reg2Value + '\'' +
                "\n , multiplier=" + multiplier +
                "\n , multiplierValue='" + multiplierValue + '\'' +
                "\n , bytePtr=" + bytePtr +
                "\n , wordPtr=" + wordPtr +
                "\n , dwordPtr=" + dwordPtr +
                "\n , farPtr=" + farPtr +
                "\n , segChange=" + segChange +
                "\n , segChangeValue='" + segChangeValue + '\'' +
                '}';
    }

    public boolean isTextConstant() {
        return textConstant;
    }

    public void setTextConstant(boolean textConstant) {
        this.textConstant = textConstant;
    }

    public String getTextConstantValue() {
        return textConstantValue;
    }

    public void setTextConstantValue(String textConstantValue) {
        this.textConstantValue = textConstantValue;
    }

    public boolean isConstant() {
        return constant;
    }

    public void setConstant(boolean constant) {
        this.constant = constant;
    }

    public String getConstantValue() {
        return constantValue;
    }

    public void setConstantValue(String constantValue) {
        this.constantValue = constantValue;
    }

    public boolean isRegister8() {
        return register8;
    }

    public void setRegister8(boolean register8) {
        this.register8 = register8;
    }

    public String getRegister8Value() {
        return register8Value;
    }

    public void setRegister8Value(String register8Value) {
        this.register8Value = register8Value;
    }

    public boolean isRegister32() {
        return register32;
    }

    public void setRegister32(boolean register32) {
        this.register32 = register32;
    }

    public String getRegister32Value() {
        return register32Value;
    }

    public void setRegister32Value(String register32Value) {
        this.register32Value = register32Value;
    }

    public boolean isDirectAddress() {
        return directAddress;
    }

    public void setDirectAddress(boolean directAddress) {
        this.directAddress = directAddress;
    }

    public String getDirectAdressValue() {
        return directAdressValue;
    }

    public void setDirectAdressValue(String directAdressValue) {
        this.directAdressValue = directAdressValue;
    }

    public boolean isBaseIndexMultiplier() {
        return baseIndexMultiplier;
    }

    public void setBaseIndexMultiplier(boolean baseIndexMultiplier) {
        this.baseIndexMultiplier = baseIndexMultiplier;
    }

    public boolean isReg1() {
        return reg1;
    }

    public void setReg1(boolean reg1) {
        this.reg1 = reg1;
    }

    public String getReg1Value() {
        return reg1Value;
    }

    public void setReg1Value(String reg1Value) {
        this.reg1Value = reg1Value;
    }

    public boolean isReg2() {
        return reg2;
    }

    public void setReg2(boolean reg2) {
        this.reg2 = reg2;
    }

    public String getReg2Value() {
        return reg2Value;
    }

    public void setReg2Value(String reg2Value) {
        this.reg2Value = reg2Value;
    }

    public boolean isMultiplier() {
        return multiplier;
    }

    public void setMultiplier(boolean multiplier) {
        this.multiplier = multiplier;
    }

    public String getMultiplierValue() {
        return multiplierValue;
    }

    public void setMultiplierValue(String multiplierValue) {
        this.multiplierValue = multiplierValue;
    }

    public boolean isBytePtr() {
        return bytePtr;
    }

    public void setBytePtr(boolean bytePtr) {
        this.bytePtr = bytePtr;
    }

    public boolean isWordPtr() {
        return wordPtr;
    }

    public void setWordPtr(boolean wordPtr) {
        this.wordPtr = wordPtr;
    }

    public boolean isDwordPtr() {
        return dwordPtr;
    }

    public void setDwordPtr(boolean dwordPtr) {
        this.dwordPtr = dwordPtr;
    }

    public boolean isFarPtr() {
        return farPtr;
    }

    public void setFarPtr(boolean farPtr) {
        this.farPtr = farPtr;
    }

    public boolean isSegChange() {
        return segChange;
    }

    public void setSegChange(boolean segChange) {
        this.segChange = segChange;
    }

    public String getSegChangeValue() {
        return segChangeValue;
    }

    public void setSegChangeValue(String segChangeValue) {
        this.segChangeValue = segChangeValue;
    }
}
