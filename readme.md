# Gradebook

-----

## Description

This project is an assignment from Objective Technologies class at the AGH University of Science and Technology. It was made to help in managing school chores such as: adding new subjects, adding new teachers and students, creating courses, creating assignments for courses, grading students. It also aims at simplifying managing students groups and their courses.

-----

### Technology used

Backend with it's endpoints is made in spring boot java framework. Database is stored in H2, the Java SQL database which is accessed by Hibernate.
Frontend is made in Vue.js framework.

-----

#### Usage

To start backend daemon, go to gradebook-backend folder and type following command:

```` txt
./gradlew bootRun
````

In order to start frontend service, you need to install vue packages. You need to be in gradebook-frontend folder and type following command:

```` txt
npm install
````

After installation stay in gradebook-frontend folder and type following command:

```` txt
npm run serve
````

-----

#### Prject structure

Sub-folders structure in project.

```` txt
.
├── gradebook-backend                                   #(main backend folder)
    ├── src                                             #(source folder)
        ├── main/java/pl/edu/agh/zarzecze/gradebook     #(package folder)
            ├── components                              #(project components)
            ├── controllers                             #(spring controllers)
            ├── exception                               #(exceptions used in project)
            ├── mail                                    #(mail sender class)
            ├── model                                   #(JPA model classes)
            ├── repository                              #(spring repositories)
            ├── security                                #(security handling classes)
            ├── services                                #(external services)
    ├── test                                            #(project tests)
├── gradebook-frontend                                  #(main frontend folder)
    ├── public                                          #(website accessed by users)
    ├── src                                             #(source folder)
        ├── api                                         #(vue.js api)
        ├── components                                  #(vue.js components)
        ├── router                                      #(vue.js routing)
        ├── store                                       #(vue.js model storage)
        ├── views                                       #(vue.js views)
├── readme_images                                       #(images used in readme)

````

-----

#### Example views

##### Login page

![login page](https://github.com/Marwin34/gradebook/blob/master/readme_images/login_img.png "Login page")

##### Administrator panel, ligt and dark mode

![administrator panel](https://github.com/Marwin34/gradebook/blob/master/readme_images/admin_panel_img.png "Administrator panel")

![administrator panel dark](https://github.com/Marwin34/gradebook/blob/master/readme_images/admin_panel_dark_img.png "Administrator panel, dark mode")

##### Account management page, light and dark mode

![account management](https://github.com/Marwin34/gradebook/blob/master/readme_images/account_management_img.png "Account management page")

![account management dark](https://github.com/Marwin34/gradebook/blob/master/readme_images/account_management_dark_img.png "Account management page, dark mode")

##### Teacher panel, light and dark mode

![teacher panel](https://github.com/Marwin34/gradebook/blob/master/readme_images/teacher_panel_img.png "Teacher panel")

![teacher panel dark](https://github.com/Marwin34/gradebook/blob/master/readme_images/teacher_panel_dark_img.png "Teacher panel, dark mode")

##### Course management panel, light and dark mode

![course management panel](https://github.com/Marwin34/gradebook/blob/master/readme_images/courses_management_img.png "course management panel")

![course management panel dark](https://github.com/Marwin34/gradebook/blob/master/readme_images/courses_management_dark_img.png "course management panel, dark mode")

##### Course details panel, light and dark mode

![course details panel](https://github.com/Marwin34/gradebook/blob/master/readme_images/course_details_img.png "course details panel")

![course details panel dark](https://github.com/Marwin34/gradebook/blob/master/readme_images/course_details_dark_img.png "course details panel, dark mode")
