# Repository for HBV501G Team 9

## Team members
- Sigurður Ari Stefánsson
- Yi Hu
- Tryggvi Konráðsson
- Jón Emil Rafnsson

## Web Service
https://hbv1-gamecatalog.onrender.com

## Execution
Here are some simple instructions on how you can run our system on your own.

Our project is designed to be run through an intelliJ IDE using Maven and Java version 24.
Maven should handle all other dependencies based on the config in the pom.xml file.
You need to setup some environment variables for the database connection, JWT config and a Cloudinary connection in order to get the project to run.
The exact variables and their names can be found in the application.properties file.
You will need a valid database and Cloudinary connection in order for the application to run.
If you want to access our databases the easiest way is to just interface with the web application hosted on render.
But we could also provide our .env file that contain our connection configs if the graders need that.
In order to run our project simply open it up in an IntelliJ IDE and run the main class.
Maven and Java Spring should handle the rest for you.
If you run into an error mentioning something about hikari pools that just means that too many people are currently connected to the database and thus a connection to it could not be established.
Simply waiting and trying at another time should fix this problem.
