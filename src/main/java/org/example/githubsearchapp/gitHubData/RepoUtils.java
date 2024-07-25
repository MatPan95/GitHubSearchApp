package org.example.githubsearchapp.gitHubData;

import org.example.githubsearchapp.gitHubData.model.Repo;

import java.util.function.Predicate;

public class RepoUtils {
    public static final Predicate<Repo> isNotFork = repo -> !repo.isFork();
}
