# 📚 Library-Management-System

The Library Management System is a fully functional application designed to provide an efficient and user-friendly solution for managing various aspects of a library. Built using **JavaFX** for the user interface and **MySQL** for database management, this system simplifies essential library operations such as book management, member tracking, borrowing/returning books, and more.

This release — **version 1.0.0** — is a big step forward: the system is now stable and ready to be used. 🚀  
And don’t worry — this isn’t the end! We’re planning regular updates and fresh features to keep things growing.


## 📑 Table of Contents
- [Features](#-features)
- [Technologies Used](#%EF%B8%8F-technologies-used)
- [Installation](#-installation)
- [After Installation](#️-after-installation)
- [Contributing](#-contributing)
- [License](#-license)

# 🔧 Features
📖 Book Management

- Add, Edit, and Delete book records

- Store and manage book details including title, author, genre, publication year, etc.

- Search and filter books based on various parameters

👤 Member Management
- Register new members and manage their profiles

- Update member details such as name, contact information, membership status, etc.

- Track member borrowing history and current borrowings

📚 Borrowing and Returning Books
- Borrow books, manage due dates, and handle book returns

- Track borrowing transactions with dates and status

- Manage overdue books and calculate fines

🔧 Admin Tools
- User management: Ban or activate users

- Edit borrowing rights: Define borrowing limits based on user roles

- Notes and Logs: Add and view system-related notes or logs for admins


# 🖥️ Technologies Used
**JavaFX:** For building the graphical user interface

**MySQL:** For managing the database

**JDBC:** For connecting JavaFX with the MySQL database

**Maven:** For project dependency management and build automation

**Environment Variables:**  For **safely** store and access database connection details like host, username, and password — without hardcoding sensitive info.

# 📦 Installation
Alright, we know, sometimes software installations can be a little... intimidating. But don't worry! We've got **```4 different installation methods```** lined up just for you. Pick the one that suits you best and get started. 😊 Do not forget to checkout 

<b> 1) EXE+JAVA+JAVAFX </b>

The easiest and most straightforward method! If you’re having any issues with Java or JavaFX, this version is your best friend. It comes bundled with both Java and JavaFX, so you won’t have to worry about anything. "Just click and go!" 🖱️✨


<b> 2) EXE + JAVAFX </b>

Already have Java installed and set up with the %JAVA_HOME% environment variable? Then this version is perfect for you. It includes only JavaFX, so no need to worry about installing Java separately. "Java’s ready, JavaFX is here!" ☕🚀


<b> 3) JAR </b>

Feeling adventurous? If you’re comfortable with the terminal, you can run the JAR file directly. Just use the following command, and you’re good to go:
"Unleash your inner terminal hero!" 💻⚡
 ```bash
java --module-path "/path/to/javafx-sdk-24/lib" --add-modules javafx.controls,javafx.fxml -jar jar_name.jar
 ```

<b> 4) Source Code </b>

And for the real pros out there — grab the source code from GitHub and run it in your favorite IDE or code editor. Only the true developers are allowed here! 🖥️👨‍💻👩‍💻 "**Play around with the code and make your own version!**" (But don’t forget to give us a ⭐ on GitHub 😏)

# ⚙️ After Installation
Congrats on getting the application installed! 🎉 Now, there are just a few more steps to get everything up and running. Don't worry, we’ll guide you through it! 😎

<hr><hr>

#### 🛠️ **1. Setting Up Your Local MySQL Database**

Since this project uses a **MySQL database** you need a program that run in your computer, **XAMPP** is a free solution that includes everything you need to run MySQL locally. 
Here's how you can set it up:

- Download XAMPP from [here](https://www.apachefriends.org/index.html).

- Install XAMPP and launch the XAMPP Control Panel.

- Start the Apache and MySQL services by clicking the Start buttons next to them.

✅ That’s it! Your database is ready to go.
<hr><hr>

#### 🌍 2. **Set Environment Variables**

😵‍💫 Sounds scary? Don’t worry — we’ve made it super easy!  
Whether you're a total **beginner** or an "**I-set-my-own-aliases-in-bash**" kind of dev, we got you covered.

<hr>

###### 🧑‍🔧 Option A: Use the Provided Script (Easy Mode™)

Not sure what environment variables even are? No worries! We’ve got you covered.

Simply run the `xampp_auto_settings.bat` file that’s included in the installation ZIP.

📁 File: `xampp_auto_settings.bat`

✅ Just double-click it, and it will automatically configure xampp default settings.

🛡️ **Note:** You can modify these values later from your system's environment settings if needed.

<hr>

###### 💻 Option B: Do It Manually (Pro Mode™)

If you prefer to set the environment variables manually, follow these steps:

###### 🪟 Windows

Open Control Panel → System → Advanced system settings -> Click Environment Variables. - >Under System Variables, click New and set theese values:

Variable name: MYSQL_URL
Variable value: localhost:3306 (this is a default value for xampp check your own program)

Variable name: MYSQL_USER
Variable value: root (this is a default value for xampp check your own program)

Variable name: MYSQL_USER
Variable value: root (this is a default value for xampp check your own program)

Variable name: MYSQL_PASS (if you use "no-password" localhost program just do not create this variable  , otherwise you need to create this (xampp default doesn't use password) )
Variable value: your_password 

✅ Click OK, and you’re all set.

###### 🐧 Linux

Honestly... if you're on Linux, you probably already know what to do. If not please google "setting environment variables" and set variables in the windows section 😊


# 🤝 Contributing

Contributions are always welcome! Whether it's bug fixes, new features, or improving documentation — we'd love your help. Just fork the repo, make your changes, and submit a pull request.

Don’t forget to open an issue if you need help or want to discuss something before contributing.

# 📄 License

This project is licensed under the **BSD 2-Clause "Simplified"** License.  
See the [LICENSE](LICENSE) file for details.
