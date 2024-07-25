package org.example.githubsearchapp.dataAccetion;

import org.example.githubsearchapp.dataAccetion.model.Repo;

import java.util.List;

public interface getUserRepos {
    List<Repo> getReposData(String userName);
}
