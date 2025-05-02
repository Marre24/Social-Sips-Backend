package com.pvt.SocialSips.quest;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.persistence.*;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Trivia.class, name = "trivia"),
        @JsonSubTypes.Type(value = Icebreaker.class, name = "icebreaker")
})
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Quest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String prompt;

    public Quest() {

    }

    public Quest(String prompt) {
        this.prompt = prompt;
    }

    public Quest(Long id, String prompt) {
        this.id = id;
        this.prompt = prompt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }
}
