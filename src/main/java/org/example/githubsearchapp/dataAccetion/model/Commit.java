package org.example.githubsearchapp.dataAccetion.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Commit(@JsonProperty("sha") String sha) { }
