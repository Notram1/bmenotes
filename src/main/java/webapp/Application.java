package webapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import webapp.domain.User;
import webapp.repositories.UserRepository;

@SpringBootApplication
public class Application {
	
	@Autowired 
	private UserRepository urepository;
	
	@Bean
	  CommandLineRunner runner() {
	    return args -> {
	      // username: "user", password: "password_user", Bcrypt encoded e.g.: https://www.browserling.com/tools/bcrypt
	      urepository.save(new User("user","$2a$10$rqsi/OW38jyDUXpbrj1TEuYykRfYH5XFM3WjPoVqcuWK/9HFn1l7q","user@gmail.com", "ROLE_USER"));
	      // username: admin password: "password_admin", Bcrypt encoded e.g.: https://www.browserling.com/tools/bcrypt
	      urepository.save(new User("admin", "$2a$10$Rae1HeucoaPRCBrzAvd00.0.oCOIjatIYwxRHYcbZlCn4bdq5wloy","admin@gmail.com", "ROLE_ADMIN"));
	      // username: "johnny", password: "johnny123", Bcrypt encoded e.g.: https://www.browserling.com/tools/bcrypt
	      urepository.save(new User("johnny","$2a$10$2OQigyx4Dib154B0qc/An.ZOm3SPUp7AoGbzKZ2F3EhM5yz5wwx9a","johnny@gmail.com", "ROLE_USER"));
	      
	    };
	}
	
	/*In order to make this app work the following steps are required beforehand:
	 * (1) Start Mariadb server as follows:
	 * 		(First time create system tables: run bin/mysql_install_db.exe)
			To start MariaDB: run bin/mysqld.exe --console
	 * (2) Start HeidiSQL
	 * (3) Login as admin - user:root
	 * (4) Create a database with name: userdb
	 * (5) In Tools/UserManager allow full access for user:mariadb (pw:mariadb123) to the userdb database.
	 * 
	 * Start http://localhost:8180/
	 */

    public static void main(String[] args) throws Throwable {
        SpringApplication.run(Application.class, args);
    }

}
