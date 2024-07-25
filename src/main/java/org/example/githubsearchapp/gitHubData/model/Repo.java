package org.example.githubsearchapp.gitHubData.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;


import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true, value = { "fork" }, allowSetters = true)
@Data
public class Repo {

    @JsonProperty("name")
    private String repositoryName;

    @JsonProperty("owner")
    private Owner owner;

    @JsonProperty("fork")
    private boolean fork;

    private List<Branch> branches;


}
