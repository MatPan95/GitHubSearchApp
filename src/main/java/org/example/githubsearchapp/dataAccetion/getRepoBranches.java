package org.example.githubsearchapp.dataAccetion;

import org.example.githubsearchapp.dataAccetion.model.Repo;

import java.util.List;

public interface getRepoBranches {
    List<Repo> getBranchesData(List<Repo> repos, String userName);
}
