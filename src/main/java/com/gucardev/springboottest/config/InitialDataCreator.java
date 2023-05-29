package com.gucardev.springboottest.config;

import com.gucardev.springboottest.model.Address;
import com.gucardev.springboottest.model.User;
import com.gucardev.springboottest.repository.AddressRepository;
import com.gucardev.springboottest.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("!test") // will work for non "test" profiles
public class InitialDataCreator implements CommandLineRunner {

  //  @Autowired
  //  private Environment env;

  @Autowired UserRepository userRepository;

  @Autowired AddressRepository addressRepository;

  @Override
  public void run(String... args) throws Exception {
    // second way to check profile
    //    String profiles[] = env.getActiveProfiles();
    //    for (String profile : profiles) {
    //      if (profile.equals("default")) {
    //
    //      }
    //    }

    User user1 =
        userRepository.save(
            User.builder().name("user1").email("mail_user@mail.com").username("user1").build());

    User user2 =
        userRepository.save(
            User.builder()
                .name("username2")
                .email("username2@mail.com")
                .username("username2")
                .build());

    User user3 =
        userRepository.save(
            User.builder()
                .name("username3")
                .email("username3@mail.com")
                .username("username3")
                .build());

    User user4 =
        userRepository.save(
            User.builder()
                .name("username4")
                .email("username4@mail.com")
                .username("username4")
                .build());

    addressRepository.save(
        Address.builder().title("addressTitle1").detail("addressDetail1").user(user1).build());
    addressRepository.save(
        Address.builder().title("addressTitle2").detail("addressDetail2").user(user1).build());
    addressRepository.save(
        Address.builder().title("addressTitle3").detail("addressDetail3").user(user2).build());
    addressRepository.save(
        Address.builder().title("addressTitle4").detail("addressDetail4").user(user3).build());
    addressRepository.save(
        Address.builder().title("addressTitle5").detail("addressDetail5").user(user4).build());
    addressRepository.save(
        Address.builder().title("addressTitle6").detail("addressDetail6").user(user4).build());
    addressRepository.save(
        Address.builder().title("addressTitle7").detail("addressDetail7").user(user4).build());
  }
}
