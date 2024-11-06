package ru.netology.diplom;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.netology.diplom.entity.UserEntity;
import ru.netology.diplom.repository.UsersRepository;



@SpringBootApplication
@RequiredArgsConstructor
public class DiplomApplication implements CommandLineRunner {

	private final UsersRepository usersRepository;

	private final PasswordEncoder passwordEncoder;


	public static void main(String[] args) {
		SpringApplication.run(DiplomApplication.class, args);
	}


	@Override
	public void run(String... args) throws Exception {
		UserEntity userEntity = new UserEntity();
		userEntity.setLogin("Alex");
		userEntity.setPassword(passwordEncoder.encode("123"));
		System.out.println(userEntity);


		usersRepository.save(userEntity);

	}

}
