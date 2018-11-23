package com.arun.tltest;

import com.arun.tltest.models.Event;
import com.arun.tltest.models.User;
import com.arun.tltest.repositories.EventRepository;
import com.arun.tltest.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.Date;

@SpringBootApplication
public class TlTestApplication {

	public static void main(String[] args) {
		SpringApplication.run(TlTestApplication.class, args);
	}

}

@Component
class DataInitializer implements CommandLineRunner {
    private UserRepository userRepository;
    private EventRepository eventRepository;
    DataInitializer(UserRepository userRepository, EventRepository eventRepository){
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        User[] users = new User[] {new User(null, "one", "ones"),
                new User(null, "two", "twos"),
                new User(null, "three", "threes")};
        Event[] events = new Event[] {
                new Event(null, "LOGIN", new Date().getTime(), "one"),
                new Event(null, "LOGOUT", new Date().getTime(), "one")
        };
        this.userRepository.deleteAll()
                .thenMany(Flux.just(users).flatMap(user -> this.userRepository.save(user)))
                .thenMany(this.userRepository.search().doOnNext(System.out::println))
                .thenMany(this.eventRepository.deleteAll())
                .thenMany(Flux.just(events).flatMap(event -> eventRepository.save(event)))
                .subscribe();
    }
}