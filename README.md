## spring boot unit & integration tests of components
- service layer
- repository
- controller unit & integration
- dto converter
- entity getter/setter
- filter/pagination/sorting specification tests
- rate limiter
- scheduler
- wiremock

### how to run all tests

```shell
 mvn test
```

### test method name format

#### format:

- **method_given_expected**

#### examples:

- create_givenNewUser_returnCreatedUser
- getById_givenNonExistentId_throwException
- findAllMailAndUserName_givenNoCondition_returnMailAndUsernames

### Example service unit tests

```java

@Test
@DisplayName("findAll returns all users with pagination")
  void findAll_givenPageable_returnUsers(){
      Page<User> usersPage=new PageImpl<>(Arrays.asList(user1,user2));

    when(userRepository.findAll(any(Specification.class),any(Pageable.class)))
    .thenReturn(usersPage);
    when(userConverter.mapToDTO(user1)).thenReturn(userDto1);
    when(userConverter.mapToDTO(user2)).thenReturn(userDto2);

    Pageable pageable=PageRequest.of(0,5);
    Page<UserDTO> result=userService.findAll("","name",Sort.Direction.ASC,pageable);

    assertEquals(2,result.getTotalElements());
    assertEquals(1,result.getTotalPages());
    }

@Test
  void getById_givenExistingId_returnUser(){
      when(userRepository.findById(existingUser.getId())).thenReturn(Optional.of(existingUser));
      User result=userService.getById(existingUser.getId());
      assertEquals(existingUser,result);
      }

@Test
  void getById_givenNonExistentId_throwException(){
      Long nonExistentId=100L;
      when(userRepository.findById(nonExistentId)).thenReturn(Optional.empty());
      assertThrows(RuntimeException.class,()->userService.getById(nonExistentId));
    }

```

### Example repository unit tests

```java

@DataJpaTest // !important annotation for repository tests
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {

  @Autowired
  private TestEntityManager entityManager;

  @Autowired
  private UserRepository userRepository;

  private User user1, user2, user3, user4;

  @BeforeEach
  public void setup() {

    user1 = User.builder().username("user1").name("name1").email("email1@test.com").build();
    ...
    user4 = User.builder().username("username4").name("name4").email("email4@test.com").build();

    entityManager.persist(user1);
    ...
    entityManager.flush();

  }

  @Test
  void searchByKeyword_givenKeyword_returnMatchingUsers() {
    Specification<User> spec = UserSpecifications.searchByKeyword("user1");
    Pageable pageable = PageRequest.of(0, 10);

    Page<User> users = userRepository.findAll(spec, pageable);

    assertEquals(1, users.getContent().size());
    assertEquals("user1", users.getContent().get(0).getUsername());

  }

  @Test
  void existsByUsernameIgnoreCase_givenNonExistingUsername_returnFalse() {
    boolean exists = userRepository.existsByUsernameIgnoreCase("non-existing-username");
    assertFalse(exists);
  }

  @Test
  void existsById_givenExistingId_returnTrue() {
    boolean exists = userRepository.existsById(user1.getId());
    assertTrue(exists);
  }
}


```

### Example controller integration tests

```java
  @Test
  void getById_givenUserId_returnsUser()throws Exception{
      long userId=1L;
      MvcResult mvcResult=
      mockMvc
      .perform(
      MockMvcRequestBuilders.get("/api/user/"+userId)
      .accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andReturn();

      String content=mvcResult.getResponse().getContentAsString();
      UserDTO userDTO=objectMapper.readValue(content,UserDTO.class);
    assertEquals(userDTO.getUsername(),"username1");
    }

```

### Example controller integration tests using WireMock

```java

WireMockServer wireMockServer=new WireMockServer(3000);

@BeforeEach
  void setupBeforeEach()throws Exception{
      .......
      // wiremock setup

      wireMockServer.start();

      List<UserDTO> mockUserDTOs=
    Arrays.asList(
    new UserDTO(1L,"User1","user1@example.com","username1"),
    new UserDTO(2L,"User2","user2@example.com","username2"));

    RestPageResponse<UserDTO> pageResponse=new RestPageResponse<>(mockUserDTOs);

    String jsonResponse=objectMapper.writeValueAsString(pageResponse);

    WireMock.configureFor("localhost",wireMockServer.port());
    stubFor(
    get(urlEqualTo("/mock/user"))
    .willReturn(
    aResponse().withHeader("Content-Type","application/json").withBody(jsonResponse)));
    }

@Test
  void differentUsers_returnsMultipleUsers()throws Exception{
      MvcResult mvcResult=
      mockMvc
      .perform(
      MockMvcRequestBuilders.get("/api/user/different-users")
      .accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andReturn();

      String content=mvcResult.getResponse().getContentAsString();
      List<UserDTO> userDTOS=objectMapper.readValue(content,new TypeReference<List<UserDTO>>(){});
    assertTrue(userDTOS.size()>0);
    }




```

### Example controller unit tests

```java

@Test
  void getById_givenUserId_returnsUser(){
      UserDTO userDTO=new UserDTO();
      when(userService.getByIdDTO(anyLong())).thenReturn(userDTO);
      ResponseEntity<UserDTO> response=userController.getById(1L);
    assertEquals(HttpStatus.OK,response.getStatusCode());
    assertEquals(userDTO,response.getBody());
    }


```

### Example validation unit tests

```java
@ParameterizedTest
@CsvSource({
    "1, testUsername, test@mail.com, testName, false",
    "2, ab, test@mail.com, testName, true",
    "3, testUsername, invalidEmail, testName, true",
    "4, testUsername, test@mail.com, , true",
    " , testUsername, test@mail.com, testName, false",
    " ,testUsernameToooooooooooooooooooooooooooooLong, test@mail.com, testName, true",
    " , , test@mail.com, testName, true",
    " , testUsername, , testName, true",
    " , testUsername, test@mail.com,  , true"
})
  void testUserRequest(
      String idInput,String username,String email,String name,boolean hasViolations){
      Long id=StringUtils.isBlank(idInput)?1L:Long.parseLong(idInput);

      UserRequest userRequest=
      UserRequest.builder().id(id).username(username).email(email).name(name).build();

      Set<ConstraintViolation<UserRequest>>violations=validator.validate(userRequest);
    assertEquals(hasViolations,!violations.isEmpty());
    }
```

### Example dto converter unit tests

```java
  @Test
  void mapToEntityTest(){
      UserRequest userRequest=new UserRequest();
      userRequest.setUsername("username");
      userRequest.setEmail("email@test.com");
      userRequest.setName("Test User");

      User user=userConverter.mapToEntity(userRequest);

      assertEquals(userRequest.getUsername(),user.getUsername());
      assertEquals(userRequest.getEmail(),user.getEmail());
      assertEquals(userRequest.getName(),user.getName());
      }

```

## resources:

[https://github.com/folksdev/movie-api](https://github.com/folksdev/movie-api)

[https://github.com/folksdev/open-weather](https://github.com/folksdev/open-weather)

[https://www.bezkoder.com/spring-boot-unit-test-jpa-repo-datajpatest/](https://www.bezkoder.com/spring-boot-unit-test-jpa-repo-datajpatest/)

[https://medium.com/free-code-camp/unit-testing-services-endpoints-and-repositories-in-spring-boot-4b7d9dc2b772](https://medium.com/free-code-camp/unit-testing-services-endpoints-and-repositories-in-spring-boot-4b7d9dc2b772)

[https://betulsahinn.medium.com/spring-boot-ile-unit-test-yazmak-f1e4fc1f3df](https://betulsahinn.medium.com/spring-boot-ile-unit-test-yazmak-f1e4fc1f3df)

[https://vladmihalcea.com/spring-jpa-dto-projection/](https://vladmihalcea.com/spring-jpa-dto-projection/)

[https://medium.com/cuddle-ai/testing-spring-boot-application-using-wiremock-and-junit-5-d514a47ab931](https://medium.com/cuddle-ai/testing-spring-boot-application-using-wiremock-and-junit-5-d514a47ab931)

[https://www.geeksforgeeks.org/how-to-use-wiremock-with-junit-test/](https://www.geeksforgeeks.org/how-to-use-wiremock-with-junit-test/)

[https://laurspilca.com/using-wiremock-for-integration-tests-in-spring-apps/](https://laurspilca.com/using-wiremock-for-integration-tests-in-spring-apps/)





