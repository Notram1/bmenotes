# BMEnotes

| ![sign_in](/src/main/resources/static/images/sign_in.png) | ![sign_up](/src/main/resources/static/images/sign_up.png) |
| ![home](/src/main/resources/static/images/home.png) | ![iframe](/src/main/resources/static/images/iframe.png) |

![egyed-kapcsolat](/src/main/resources/static/images/egyed-kapcsolat.png)


## How to start application

1. Start Mariadb server as follows:
	- (First time create system tables: run bin/mysql_install_db.exe)
	- To start MariaDB: run bin/mysqld.exe --console
2. Start HeidiSQL (to be able to see what is going on in your database)
3. Start http://localhost:8180/home

## Usage

Step by step tutorial:
1. See ‘How to start the application’ above
2. The server will direct you to the login page
	- Login with existing username and password combination
	- Create an account
3. If the login was successful, you will be directed to the main page.
4. To check user data, click on the user icon on the right
	- You can edit and delete your account here
5. To check the users subjects click on the icon on the left	
	- You can open/delete uploaded materials and add new ones
6. To add new material use the drag&drop icon and follow the instructions

For informations check out the footer.

## Further development 

1. Now each user has his own database with his files and profile. It would be useful if he could explore friends, classmates online and they could have shared files.
2. Now each user can add new subjects, but can’t explore courses from friends.
3. The user could see a calendar, where he could see the upcoming events, like conferences
4. There could be online tests for each subject, where the users could test their knowledge.

## Authors

The project was created by students of BME, Faculty of Mechanical Engineering, Mechatronics MSc for the subject ‘Webes alkalmazások fejlesztése’, mentored by Dr. Janos Hamar.

Developers:
- Anna Bodonhelyi
- Marton Szep
- Marton Cserni
- Abel Kocsis

## Licence

The used softwares were open source or licenced by BME for academic use.
