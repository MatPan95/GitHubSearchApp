package org.example.githubsearchapp.gitHubDataAccetion;

import org.example.githubsearchapp.gitHubDataAccetion.model.Repo;

import java.util.function.Predicate;

public class RepoUtils {
    public static final Predicate<Repo> isNotFork = repo -> !repo.isFork();
}
