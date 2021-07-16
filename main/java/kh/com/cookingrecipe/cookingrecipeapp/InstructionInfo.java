package kh.com.cookingrecipe.cookingrecipeapp;

public class InstructionInfo {

    public InstructionInfo(String instruction_name, String instruction_detail) {
        this.instruction_name = instruction_name;
        this.instruction_detail = instruction_detail;
    }

    public InstructionInfo(){

    }

    public String getInstruction_name() {
        return instruction_name;
    }

    public void setInstruction_name(String instruction_name) {
        this.instruction_name = instruction_name;
    }

    private String instruction_name;

    public String getInstruction_detail() {
        return instruction_detail;
    }

    public void setInstruction_detail(String instruction_detail) {
        this.instruction_detail = instruction_detail;
    }

    private String instruction_detail;

}
