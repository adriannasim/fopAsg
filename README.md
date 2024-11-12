# DIGITAL DIARY ASSIGNMENT
## Directory
1. [Assignment Topic PDF](#Assignment-Topic)
2. [Workflow](#Workflow)
   - [Before starting a task](#Before-Starting-a-Task)
3. [Setting Up](#How-To-Setup-GitGitHub)
   - [Setting Up GitHub](#How-To-Setup-GitGitHub)
     * [In VSCode](#In-VSCode)
     * [In NetBeans](#In-Netbeans)
<br/>

## [Assignment Topic](https://github.com/user-attachments/files/17698336/WIX1002.Assignment.Topic.7.PDF)

## Workflow
### Before Starting a Task
Merge conflicts are the most common issue when everyone is working on the same project. So to prevent this issue, before starting on your module or task, please create a branch first:
1. Go on GitHub in your browser and to the page of our repo. Click on Branch and then New Branch.
    <br/>![image](https://github.com/user-attachments/assets/dfa81910-8835-4002-ad0c-d51f37a60731)
    <br/>![image](https://github.com/user-attachments/assets/32e320cb-1be3-4af2-9c1b-3e95c8bcf834)
2. For the branch name, type in whichever module that you are working on (Eg. new-diary-page) and for the source, choose main. (If you are fixing a branch, you can change the source to the branch that you are trying to fix and name it fixing-new-diary-page or something like that). Then click on create new branch.
    <br/>![image](https://github.com/user-attachments/assets/0ece6854-9cd0-4872-a8a2-b5b8f1ad5475)
3. Now, go to GitHub Desktop and switch to the branch. Now you can proceed to whatever you are planning to do without disrupting other's work!
    <br/>![image](https://github.com/user-attachments/assets/98657d8c-b499-405c-a0eb-61621f25b1b8)

<br/>Now if you are done with your work, you can start a PR or Pull Request:
1. Go on GitHub in your browser and to the page of our repo. At the navigation bar click on Pull Requests and then New Pull Requests.
   <br/>![image](https://github.com/user-attachments/assets/2cc921f2-33f1-4e6a-8a61-267b3bac50a1)
3. Choose the base as the branch that you want to merge into, and then for the compare, choose the branch that you want it to merge to the base. Once done, click on Create Pull Request.
    <br/>![image](https://github.com/user-attachments/assets/76aef9dd-8d7d-44fc-8efd-105ba6c8f462)
4. Write your title (Eg. New Diary Page done) and anything you want to add on in the description. Once done, click on Create Pull Request.
5. **DO NOT CLICK ON MERGE PULL REQUEST!** I'll check on pull requests from time to time, in case anything needs fixing. Once I approve the PR, then you may merge to main. Yay!

## How To Setup Git/GitHub
I prefer using VSCode because the Git interface is better, but you do you, choose any IDE based on your own preference! But I only know how to set up Git in VSCode and NetBeans so I will only cover this 2.
### In VSCode
1. Download GitHub Desktop
2. In GitHub Desktop, click on clone repository:
   <br/>![image](https://github.com/user-attachments/assets/57aa9db4-33b1-491e-9134-e63205fc780b)
4. Find our repository (adriannasim/fopAsg) and click clone
   <br/>![image](https://github.com/user-attachments/assets/b1bd084d-78df-4594-8b9f-b4f9dd429423)
5. That's it! Now you can open in VSCode!

### In Netbeans
1. Open GitHub in your browser
2. Click on your profile picture at the top right corner and settings.
   <br/>![image](https://github.com/user-attachments/assets/58fc1029-d189-4a2d-9089-b09929f78250)
3. On the left navigation tab, click on Developer Settings (might have to scroll until the bottom)
   <br/>![image](https://github.com/user-attachments/assets/162e1651-89d8-4872-b2ec-cf09ae2c1426)
4. Click on Personal Access Tokens and then Tokens (classic)
   <br/>![image](https://github.com/user-attachments/assets/5ed802fb-59b0-44db-804f-bd5a08c0deb3)
5. Click on Generate New Token, then Generate New Token (classic)
   <br/>![image](https://github.com/user-attachments/assets/3d6375c3-8294-4461-86c0-c3a55e337dd0)
6. In the New Personal Access Token page, enter anything for the Note field (Eg. netbeansgit). Set expiration to No expiration, and tick everything. Once done, click on Generate Token.
7. **THIS IS THE IMPORTANT PART!!!** Copy your personal access token and then paste it somewhere safe. Then we will move on to Netbeans.
8. In Netbeans, click on the Team tab, then hover over Git, then click on Clone...
   <br/>![image](https://github.com/user-attachments/assets/fe611851-bfe3-4d09-b397-952e375ac6dc)
9. On the Repository URL, enter this: **https://github.com/adriannasim/fopAsg.git**
10. For the username, you can enter anything you like (preferrably your own name so we can know) and for the password, paste in the personal access token that you have just copied. **PLEASE TICK THE SAVE PASSWORD BOX** For the destination folder, you can put it anywhere you like or just leave it at the default path. Lastly click on Next.
    <br/>![image](https://github.com/user-attachments/assets/b72605b5-2980-4eb5-bcb2-b0e37e90767d)
12. Click Select All remote branches and click on Next.
13. The next page will be something like this:
    <br/>![image](https://github.com/user-attachments/assets/1a96f355-699c-4ef9-8fdd-8428b452a603)
    <br/>If it's correct, click on Finish.
14. Once cloning is done, click on Create Project on the pop up dialog.
    <br/>![image](https://github.com/user-attachments/assets/292179cd-a5e1-4a78-8d92-26272a6048f6)
15. Name your project anything you like (Eg. fopAsg), then click on Finish.
    <br/>![image](https://github.com/user-attachments/assets/aaee9cc1-70d3-4cdd-abd6-06459bac3128)
    
<br/>*For performing git commands, I don't really like using Netbeans to do so, but that is totally up to you. For me, I always try to use the GitHub Desktop software because I like the user interface better, so here's how to:*
1. In GitHub Desktop, click on Add Local Repository...
   <br/> ![image](https://github.com/user-attachments/assets/9e625521-4a4b-49ec-bcf2-db01041a55da)
2. Find your project URL (it should be something like C:\Users\XXX\Documents\NetBeansProjects\fopAsg if you left it by default when you clone it just now) then click Select Folder.
3. Then finally click on Add Repository.
4. Now when you make any changes in NetBeans, it will be reflected on GitHub Desktop so you don't have to use the crappy and messy git interface in NetBeans!



