## issues while testing:

- public/private/protected methods test?
- static methods test?
- cron jobs
- interface (dao) / service interface
- repository
- entity/dto converter
- filter/pagination/sorting specification tests
- wiremock

### test method name format

#### format:

- **method_given_expected**

#### examples:
- create_givenNewUser_returnCreatedUser
- getById_givenNonExistentId_throwException
- findAllMailAndUserName_givenNoCondition_returnMailAndUsernames

## resources:

[https://www.bezkoder.com/spring-boot-unit-test-jpa-repo-datajpatest/](https://www.bezkoder.com/spring-boot-unit-test-jpa-repo-datajpatest/)

[https://medium.com/free-code-camp/unit-testing-services-endpoints-and-repositories-in-spring-boot-4b7d9dc2b772](https://medium.com/free-code-camp/unit-testing-services-endpoints-and-repositories-in-spring-boot-4b7d9dc2b772)

[https://betulsahinn.medium.com/spring-boot-ile-unit-test-yazmak-f1e4fc1f3df](https://betulsahinn.medium.com/spring-boot-ile-unit-test-yazmak-f1e4fc1f3df)

[https://github.com/folksdev/movie-api](https://github.com/folksdev/movie-api)

[https://vladmihalcea.com/spring-jpa-dto-projection/](https://vladmihalcea.com/spring-jpa-dto-projection/)

[https://medium.com/cuddle-ai/testing-spring-boot-application-using-wiremock-and-junit-5-d514a47ab931](https://medium.com/cuddle-ai/testing-spring-boot-application-using-wiremock-and-junit-5-d514a47ab931)

[https://www.geeksforgeeks.org/how-to-use-wiremock-with-junit-test/](https://www.geeksforgeeks.org/how-to-use-wiremock-with-junit-test/)

[https://laurspilca.com/using-wiremock-for-integration-tests-in-spring-apps/](https://laurspilca.com/using-wiremock-for-integration-tests-in-spring-apps/)





