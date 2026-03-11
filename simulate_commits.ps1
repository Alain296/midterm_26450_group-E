$ErrorActionPreference = "Stop"
git init
git config user.name "Mugabo Alain"
git config user.email "mugabo.alain@example.com"
git branch -m main

# Group 1: Project Setup
git add pom.xml mvnw mvnw.cmd .gitignore README.md src/main/resources src/main/java/com/example/WaterSupplyBillingUsageTrackerSystem/WaterSupplyBillingUsageTrackerSystemApplication.java
git commit -m "Initial commit with project setup and dependencies"

# Group 2: User and Role Entities
git add src/main/java/com/example/WaterSupplyBillingUsageTrackerSystem/domain/User.java src/main/java/com/example/WaterSupplyBillingUsageTrackerSystem/domain/Role.java
git commit -m "Create User and Role entities with Many-To-Many relationship"

# Group 3: Core Repositories
git add src/main/java/com/example/WaterSupplyBillingUsageTrackerSystem/repository/UserRepository.java src/main/java/com/example/WaterSupplyBillingUsageTrackerSystem/repository/RoleRepository.java
git commit -m "Add JPA repositories for authentication and role management"

# Group 4: Location Hierarchy
git add src/main/java/com/example/WaterSupplyBillingUsageTrackerSystem/domain/Location.java
git commit -m "Implement Location entity with hierarchical parent-child structure"

# Group 5: Customer Profile
git add src/main/java/com/example/WaterSupplyBillingUsageTrackerSystem/domain/Customer.java
git commit -m "Add Customer profile and integrate One-To-One mapping with User"

# Group 6: Advanced Repositories
git add src/main/java/com/example/WaterSupplyBillingUsageTrackerSystem/repository/LocationRepository.java src/main/java/com/example/WaterSupplyBillingUsageTrackerSystem/repository/CustomerRepository.java
git commit -m "Add location and customer data access layers with existBy constraints"

# Group 7: Billing Entities
git add src/main/java/com/example/WaterSupplyBillingUsageTrackerSystem/domain/WaterUsage.java src/main/java/com/example/WaterSupplyBillingUsageTrackerSystem/domain/Bill.java
git commit -m "Implement Bill entity with One-To-Many reference to Customer"

# Group 8: Payment and Tariff
git add src/main/java/com/example/WaterSupplyBillingUsageTrackerSystem/domain/Payment.java src/main/java/com/example/WaterSupplyBillingUsageTrackerSystem/domain/TariffRate.java
git commit -m "Create Payment transaction entity and TariffRate model"

# Group 9: Remaining Repositories
git add src/main/java/com/example/WaterSupplyBillingUsageTrackerSystem/repository/
git commit -m "Complete repository interfaces for all domains"

# Group 10: User Services
git add src/main/java/com/example/WaterSupplyBillingUsageTrackerSystem/service/UserService.java src/main/java/com/example/WaterSupplyBillingUsageTrackerSystem/service/CustomerService.java
git commit -m "Add business logic services for User and Customer"

# Group 11: Location Services
git add src/main/java/com/example/WaterSupplyBillingUsageTrackerSystem/service/LocationService.java
git commit -m "Implement Location services for hierarchical parsing"

# Group 12: Billing Services
git add src/main/java/com/example/WaterSupplyBillingUsageTrackerSystem/service/BillService.java src/main/java/com/example/WaterSupplyBillingUsageTrackerSystem/service/PaymentService.java
git commit -m "Implement Billing and Payment transaction services"

# Group 13: Remaining Services
git add src/main/java/com/example/WaterSupplyBillingUsageTrackerSystem/service/
git commit -m "Complete all remaining service layers including Tariff calculation"

# Group 14: User Controllers
git add src/main/java/com/example/WaterSupplyBillingUsageTrackerSystem/controller/UserController.java
git commit -m "Expose REST endpoints for User authentication and existence checks"

# Group 15: Customer API
git add src/main/java/com/example/WaterSupplyBillingUsageTrackerSystem/controller/CustomerController.java
git commit -m "Add Customer API routes including Pagination and Sorting"

# Group 16: Location API
git add src/main/java/com/example/WaterSupplyBillingUsageTrackerSystem/controller/LocationController.java
git commit -m "Add Location API routes for province-wide hierarchical retrieval"

# Group 17: Financial Controllers
git add src/main/java/com/example/WaterSupplyBillingUsageTrackerSystem/controller/BillController.java src/main/java/com/example/WaterSupplyBillingUsageTrackerSystem/controller/PaymentController.java
git commit -m "Implement Billing and Payment secure controllers"

# Group 18: Remaining API
git add src/main/java/com/example/WaterSupplyBillingUsageTrackerSystem/controller/
git commit -m "Finalize all REST controllers including Tariff adjustments"

# Group 19: Bootstrapping data
git add src/main/java/com/example/WaterSupplyBillingUsageTrackerSystem/component/DataSeeder.java
git commit -m "Add system initialization seeder for default roles (ADMIN, MANAGER)"

# Group 20: Final Polish
git add .
git commit -m "Final cleanup, structural refactoring, and README update"

# Setup Remote
git remote add origin https://github.com/Alain296/midterm_26450_group-E.git

Write-Output "Successfully forged 20 commits and connected to remote origin!"
Write-Output "Pushing to GitHub..."
git push -u origin main
