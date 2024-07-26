package org.example.githubsearchapp.dataAccetion.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Branch {

    @JsonProperty("name")
    private String branchName;

    @JsonProperty("commit")
    private Commit commit;
}
