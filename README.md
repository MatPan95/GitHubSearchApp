Application for Retrieving Repository Information

This application is designed to retrieve information about repositories belonging to a specified user.
The requirements are that the repositories must be public and not forks. The returned information
includes the owner's login, the repository name, the names of all branches in the repository, and the
SHA of the latest commit. The required header is: accept: application/json. If the user has no
repositories that meet the criteria, an empty JSON object is returned.

Response Format:

[
 {
 "branches": [
 {
 "name": "${branchName}",
 "commit": {
 "sha": "${sha}"
 }
 }
 ],
 "name": "${repositoryName}",
 "owner": {
 "login": "${userLogin}"
 }
 }
]

Error Format:

{
 "status": "${responseCode}",
 "message": "${whyHasItHappened}"
}

Endpoint for Querying:

localhost:8080/{userName}

Where {userName} is the username of the GitHub user whose repositories you want to query.

Application Requirements:

- Java 21
- Spring Boot 3.2
- JUnit5
- JMeter

Request Processing Flow:

1. Media Type Validation: The media type from the accept header in the HTTP request from the client is checked.
    - Service Call: mediaTypeValidationService.validateMediaType(mediaType);
    - Exception Handling: If the media type does not match, an appropriate exception is thrown and information is returned to the client.

2. User Name Validation: The username is validated against GitHub’s current criteria for usernames as of 30.07.24.
    - Service Call: userNameValidationService.validateUser(userName);
    - Exception Handling: If the username does not meet the criteria, an appropriate exception is thrown and information is returned to the client.

3. Fetching Repository Data: The method appService.getUserRepos(userName) is called to fetch repository data.
    - Response: return new ResponseEntity<>(appService.getUserRepos(userName), HttpStatus.OK);

4. Repository Data Retrieval in Service:
    - A List<Repo> object is created based on the method getReposData.
    - Method Call: List<Repo> repos = gitHubGetRepos.getReposData(userName);

5. Method getReposData:
    - Performs a GET request using RestClient.
    - Request Details:
        - URI: githubURIs.reposURI().replace("{username}", userName)
        - Accept Header: MediaType.APPLICATION_JSON
        - Response Handling:
            - .onStatus(status -> status.value() == 404, (request, response) -> { throw new UserNotFoundException(); })
            - Converts JSON response to a List<Repo> object using Jackson annotations.

6. Filtering Repositories: Fork repositories are removed from the list.
    - Code: repos.removeIf(Repo::isFork);
    
7. Empty List Handling: If the list is empty, an empty JSON object is returned.
    - Code: if (repos.isEmpty()) return repos;

8. Branch Data Retrieval:
    - Method Call: return gitHubGetBranches.getBranchesData(repos, userName);
    - The method is similar to getReposData but uses a different endpoint and replaces one additional fragment.

8. Response Handling: The list of objects is serialized to JSON and returned to the client with a 200 status code.

Additional Information

- There is no public GitHub endpoint to check if a username meets basic username requirements, so this is handled "hardcoded" in the application. This avoids making an additional request to GitHub’s API just to verify the username.

- The endpoint "https://api.github.com/users/{username}/repos" is used instead of "https://api.github.com/search/repositories?q=user:{username}" because it performs better with such a simple filter. Filtering is done at the API level rather than using a more resource-intensive search endpoint.

- To improve performance, virtual threads and a 1-minute cache are used for handling GET requests to the application API. Virtual threads are also used for handling one of GitHub's endpoints (getBranchesData) and filtering responses using parallel streams.
