
package com.team.karyera.dto;

public class AskResponse {

    private String answer;
    private String source;

    public AskResponse() {
    }

    public AskResponse(String answer, String source) {
        this.answer = answer;
        this.source = source;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
