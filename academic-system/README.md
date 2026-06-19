## User Story Roadmap


### Academic Features

| ID                  | User Story                                       | Status |
|---------------------|--------------------------------------------------|--------|
| US-2361             | Register assessments in classes                  | ✅      |
| US-2363             | Register classes through keyboard input          | ✅      |
| US-2364             | Manage academic system through command line menu | ✅      |
| US-2375             | Generate class assessment summary report         | ✅      |
| US-2376             | Generate assessment weight report                | ✅      |

### Persistence Features

| ID       | User Story                                  | Status |
|----------|---------------------------------------------|--------|
| TUS-2362 | Persist class assessments to TXT file       | ✅      |
| US-2372  | Configure persistence type as administrator | ✅      |
| US-2373  | Save academic data to XML file              | ✅      |
| US-2374  | Save academic data to JSON file             | ✅      |
| US-2377  | Generate persistence configuration report   | ✅      |

### Security Features

| ID                  | User Story                                                            | Status |
|---------------------|-----------------------------------------------------------------------|--------|
| US-2366             | Authenticate users and authorize actions based on roles               |        |
| US-2369             | Handle authentication and authorization errors with custom exceptions |        |
| US-2378             | Role-based dynamic menu rendering                                     |        |
| US-2379             | Logout                                                                |        |
| US-2380             | Display role-specific sequential menus                                |        |

### Validation and Exception Handling

| ID       | User Story                                                     | Status |
|----------|----------------------------------------------------------------|--------|
| US-2367  | Handle academic domain errors with custom exceptions           |        |
| US-2368  | Handle keyboard input errors with custom exceptions            |        |
| TUS-2371 | Validate academic domain objects using Jakarta Bean Validation |        |

### Architecture and Refactoring

| ID       | User Story                                             | Status |
|----------|--------------------------------------------------------|--------|
| US-0000  | Start academic system                                  | ✅      |
| TUS-     | Refactor domain model using Lombok                     |        |
| TUS-2370 | Refactor menu operations into AcademicSystemController |        |
| TUS-2382 | Define equality for identifiable domain objects        |        |
| TUS-2396 | Introduce ClassService                                 |        |
| TUS-2397 | Introduce AssessmentService                            |        |
| TUS-2398 | Introduce PersistenceService                           |        |
| TUS-2399 | Introduce ReportService                                |        |
| TUS-2400 | Simplify AcademicSystemController                      |        |
| TUS-2414 | Introduce AuthenticationController for JavaFX login    |        |

### Docker and Deployment

| ID       | User Story                          | Status |
|----------|-------------------------------------|--------|
| TUS-2381 | Deliver academic system with Docker |        |

### Testing Infrastructure and Automated Tests

| ID       | User Story                                        | Status |
|----------|---------------------------------------------------|--------|
| TUS-2383 | Configure automated testing infrastructure        |        |
| TUS-2384 | Test identifiable domain object equality          |        |
| TUS-2385 | Test academic domain validation                   |        |
| US-2386  | Test authentication behavior                      |        |
| US-2387  | Test authorization behavior                       |        |
| US-2388  | Test report generation                            |        |
| US-2389  | Test persistence repositories                     |        |
| TUS-2395 | Verify logging infrastructure behavior            |        |
| TUS-2401 | Test ClassService behavior                        |        |
| TUS-2402 | Test AssessmentService behavior                   |        |
| TUS-2403 | Test PersistenceService behavior                  |        |
| TUS-2404 | Test ReportService behavior                       |        |
| TUS-2405 | Test AcademicSystemController delegation behavior |        |

### Logging and Auditing

| ID       | User Story                                   | Status |
|----------|----------------------------------------------|--------|
| TUS-2390 | Configure application logging infrastructure |        |
| TUS-2391 | Log authentication and logout events         |        |
| TUS-2392 | Log authorization failures                   |        |
| TUS-2393 | Log persistence operations                   |        |
| TUS-2394 | Log report generation                        |        |

### Graphical User Interface (JavaFX)

| ID       | User Story                                              | Status |
|----------|---------------------------------------------------------|--------|
| TUS-2406 | Configure JavaFX application infrastructure             |        |
| TUS-2407 | Create JavaFX login screen                              |        |
| TUS-2408 | Create JavaFX role-based main screen                    |        |
| TUS-2409 | Create JavaFX class registration screen                 |        |
| TUS-2410 | Create JavaFX assessment registration screen            |        |
| TUS-2411 | Create JavaFX report screen                             |        |
| TUS-2412 | Create JavaFX persistence configuration screen          |        |
| TUS-2413 | Create JavaFX class and assessment visualization screen |        |

### CI/CD and Automation

| ID       | User Story                                    | Status |
|----------|-----------------------------------------------|--------|
| TUS-2415 | Configure CI pipeline with GitHub Actions     |        |
| TUS-2416 | Generate test coverage reports                |        |
| TUS-2417 | Publish Docker image automatically            |        |
| TUS-2418 | Configure pull request validation workflow    |        |
| TUS-2419 | Configure release workflow                    |        |
| TUS-2420 | Configure branch protection for pull requests |        |
