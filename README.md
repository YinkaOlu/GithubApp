
# GitHub App 
![android_ci](https://github.com/YinkaOlu/GithubApp/workflows/android_ci/badge.svg)

## Summary
This is an Android application that displays publicly available Github User information and their list of public Github Repositories. 

[API details for Github Endpoints](https://developer.github.com/v3/users/)

## App Structure

### Data
#### Models
The models are a representation of the expected information coming from Github API. The Github User and Repository Models in this project are simplified versions of the full JSON response.
#### Github Data Provider
Github Data Provider is reponsible for getting the information from the data source, in this case the Github API.
#### Github Repository
The Github Repository acts as the controller between the raw data coming from the data provider and the rest of the application/ViewModels. 
The repository knows where the data is stored and how to get the information it needs.

### ViewModel
#### GithubViewModel
The role of the GithubViewModel is to determine what should be done with data coming from the Github Repository. 
It's also reponsible for determining what should happen on user interactions (ie, taking the input search text and sending to correct repository functions).

### View
#### MainActivity
This activity will hold the ViewModel that will be used to manage the Github user detail views and Error views. The activity will also contain logic to handle the live data coming from the ViewModel and pass information to the appropriate sub-views. It will also pass input data from the user to the ViewModel to handle appropriately.

#### RepoListFragment
List of Github Repository data from the MainActivity is passed to this fragment via Bundle. This fragment is responsible for presentation logic for displaying the Github Repository.

#### Error
Error information is passed from the MainActivity to this fragment. This fragment is responsible for presenting the error information to the user.
