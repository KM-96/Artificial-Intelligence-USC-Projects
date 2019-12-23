package model;

public class ResolutionState {
    private String status = LogicConstants.RESOLUTION_FAIL;

    private ComplexSentence complexSentence;

    public ResolutionState() {
        this.complexSentence = new ComplexSentence();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ComplexSentence getComplexSentence() {
        return complexSentence;
    }

    public void setComplexSentence(ComplexSentence complexSentence) {
        this.complexSentence = complexSentence;
    }
}

