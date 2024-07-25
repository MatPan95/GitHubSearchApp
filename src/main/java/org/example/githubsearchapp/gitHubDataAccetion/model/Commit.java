package org.example.githubsearchapp.gitHubDataAccetion.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
record Commit(@JsonProperty("sha") String sha) { }
