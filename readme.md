# Gradebook

-----

### Description

This project is an assignment from Objective Technologies class at the AGH University of Science and Technology. It was made to help in managing school chores such ass: adding new subjects, adding new teachers and students, creating curses, creating assignments for courses, grading students. It also aims at simplifing managing students groups and their courses.

-----

### Technology used

Backend with it's endpoints is made in java using spring boot. Database is stored in H2, the Java SQL database which is accessed by Hibernate.
Frontend is made in Vue.js framework.

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
