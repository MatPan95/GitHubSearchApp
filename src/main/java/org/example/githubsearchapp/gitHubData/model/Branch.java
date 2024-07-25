package org.example.githubsearchapp.gitHubData.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
public class Branch {

    @JsonProperty("name")
    private String branchName;

    @JsonProperty("commit")
    private Commit commit;
}
