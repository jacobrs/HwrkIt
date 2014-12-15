HwrkIt (Android)
======
##### Developed by: Benjamin Barault, Jacob Gagne, Alex Genio, Shane Steszyn

#### About This Project

This project will give students and teachers the ability to monitor, analyze, and make crucial changes to the students' time management strategies with regards to homework and studying. Faculty members will be provided with a way of overseeing the students’ study habits for their classes in particular in a statistical format, and the students themselves can assess their own homework habits. As a result, students will be able to manage their time more efficiently when it comes to doing homework. <br><br>

#### Screenshots

<img src="http://linux2-cs.johnabbott.qc.ca/~cs616_f14_6/screens/s2.png" width="200px" />
<img src="http://linux2-cs.johnabbott.qc.ca/~cs616_f14_6/screens/s3.png" width="200px" />
<img src="http://linux2-cs.johnabbott.qc.ca/~cs616_f14_6/screens/s1.png" width="200px" /><br><br>

#### How to Install

* Download the .apk file from [Here](https://drive.google.com/file/d/0B2d7QKcZf3UXb3I3Q0ZkRFJzM2M/view?usp=sharing)
* If you downloaded the apk onto your computer, transfer it to your phone
* On your phone use a file explorer app and navigate to the place where you stored the apk file
* Then click on the apk and the installation should commence<br><br>

#### A Bit More About the App

* **The main fragment** will be using a drawer menu with classes, homework, statistics and a logout tab. These will be linked to separate activities described below. <br><br>
* **The Classes** tab will consist of a listview with all the classes accessible to the users. Once a list element is clicked, the app will switch to a statistics view showing the student’s time spent and the homework blocks he/she inserted. Students will also have the round “add a homework time” button in the bottom right that shows up in the main fragment.<br><br>
* **The Homework** tab will show the a list of inputed homework times, this is where the user can add homework time for indivdual classes by choosing a start and end time<br><br>
* **The Statistics** tab will display different things dependent on the type of user.However if a student is logged in, the app will display personal averages and statistics based on the homework times they have inserted for ALL classes. This is a sort of overview tab.<br><br>
* **The Logout** tab will delete the user from the SQLite local database, destroy the HTTP cookie store and bring the user to the Login screen.<br><br>

#### Starting the server

* To start the server copy the jar file from [here](https://github.com/jacobrs/HwrkIt/blob/master/rest-server-0.1.0.jar) to your server
* Run it using this command (Replace "PORT" with the port you want to use):
```
	java -jar rest-server-0.1.0.jar --server.port=PORT
```

* To make it run in the background press ctrl-z to stop the job and then run these commands:
```
	jobs
	bg %job_number
```
Where "job_number" is the number of the job

#### Point to Your Spring Server

To point the app to your spring server and work on a forked version, replace the URLs in async tasks
```
	urlCheck = new URL("YOUR DOMAIN HERE");
	HttpURLConnection conCheck = (HttpURLConnection) urlCheck.openConnection();
	conCheck.setRequestMethod("GET");
```
<br>
#### Mysql Dumps

* Download dump file with starter data [here](https://drive.google.com/file/d/0By8SOmpQcCoKNkhHbjhFOWdIY1k/view?usp=sharing)
* Download dump file of just structure [here](https://drive.google.com/file/d/0B2d7QKcZf3UXb3I3Q0ZkRFJzM2M/view?usp=sharing)<br><br>

#### Operating Environments

* Android phone or tablet running Ice Cream Sandwitch is needed

*Libraries used:*
- https://android.googlesource.com/platform/frameworks/opt/datetimepicker/+/master
- https://bitbucket.org/danielnadeau/holographlibrary/wiki/Home
