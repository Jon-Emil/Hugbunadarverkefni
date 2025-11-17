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

## Execution

Follow these steps to run our system locally.

### 1. Requirements

- **IDE:** IntelliJ IDEA  
- **Build tool:** Maven  
- **Java version:** 24  
- **Framework:** Spring (configured via Maven)

Maven will automatically download and manage all other dependencies based on the configuration in `pom.xml`.

---

### 2. Environment variables

You need to set up several environment variables before the application can start:

- **Database connection**
- **JWT configuration**
- **Cloudinary connection**

The exact variable names and expected values can be found in the `application.properties` file.

> Note: You must have access to a valid database and a working Cloudinary account for the application to run.

If you would like to use our existing configuration:

- The easiest way is to use the **web application hosted on Render**, which is already connected to our databases.  
- Alternatively, we can provide our `.env` file with the connection settings if the graders need it.

---

### 3. Running the application

1. Open the project in **IntelliJ IDEA**.  
2. Make sure the required **environment variables** are set.  
3. Ensure you are using **Java 24** as the project SDK.  
4. Run the **main class** of the application.

Maven and Spring will handle the rest of the setup and startup process.

---

### 4. Common issue: Hikari pool error

If you encounter an error mentioning **Hikari pools**, it usually means:

> Too many clients are currently connected to the database, so a new connection cannot be established.

In that case, try again later when fewer people are connected to the database.
