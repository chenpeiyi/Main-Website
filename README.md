[![Build Status](https://semaphoreci.com/api/v1/daviscsclub/main-website/branches/production/shields_badge.svg)](https://semaphoreci.com/daviscsclub/main-website) 
[![Build Status](https://semaphoreci.com/api/v1/daviscsclub/main-website/branches/preview/shields_badge.svg)](https://semaphoreci.com/daviscsclub/main-website)
[![Build Status](https://semaphoreci.com/api/v1/daviscsclub/main-website/branches/integration/shields_badge.svg)](https://semaphoreci.com/daviscsclub/main-website)
[![codebeat badge](https://codebeat.co/badges/c8a41af8-bad4-4c94-a3ef-cb9bcff8286b)](https://codebeat.co/projects/github-com-daviscsclub-main-website)

# IMPORTANT

Because of licenses on a few of our HTML templates, the resources directory is made into its own private repository. Contact any of the repository owners for access.

## Terminologies
- Root Repository: This is the repository that holds the other repositories as submodules. It is also the root directory of the overall project. This code is held in Github. 
- Resource Repository: This is the repository that holds all the resources. HTML, CSS, JS, properties, etc.

## Setup
1. Have a Github account and a Bitbucket account ready with your ssh keys registered to each.
2. Contact one of the webdev team members to have your account added to both the main code repository and the resource repository.
3. Perform `git clone git@github.com:DavisCSClub/Main-Website.git` in any location.
4. Perform `git clone git@bitbucket.org:tktong/resources.git` in any location.
5. Copy `gradle.local.properties` into directory $USER_HOME/.gradle/`. Create the directory if it does not exists.
6. Rename your copy in `$USER_HOME/.gradle/` to `gradle.properties`. When running gradle, this will take override the values within the project's `gradle.properties`.
7. In your copy, change the values of `org.dcsc.template.directory` to point to the resource repository's template directory - `<resource-root>/templates/`.
8. Repeat step 7 for `org.dcsc.resource.directory` except point to the resource repository's static directory - `<resource-root>/static/`.

Here is a sample of mine on a Windows device where D:/Projects/dcsc/resources is the resource repository's root directory.
```
org.dcsc.template.directory=D:/Projects/dcsc/resources/templates/
org.dcsc.resource.directory=D:/Projects/dcsc/resources/static/
```

## Running Locally
- `gradlew bootRun` (Compiles and runs the build on `localhost:8080`)

## Committing Changes
Commit messages should start with a number sign (#) followed by the github issue number and then a summary of the changes. Example: `#123 Made a change here and there`

## Pipeline
Even though we have three branches, we deploy nightly at 1:00 a.m. PST. Any commits to integration branch will run through the build. If the build passes, it will be promoted to preview (This branch acts as our release candidate). Every day at 12:30 a.m., our resource repository's preview/release candidate branch is merged into the production branch. Every day at 1:00 a.m. PST, our codebase repository's preview branch is merged into the production branch and deployed. 
