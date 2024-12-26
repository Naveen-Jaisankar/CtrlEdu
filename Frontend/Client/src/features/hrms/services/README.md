The services folder is where the business logic and API interactions for this feature are encapsulated. Service files are responsible for making HTTP requests to the backend API and handling the feature's core functionality.

Examples of services for hrms might include:

API Calls: Functions to fetch, create, update, or delete data related to HR (e.g., getAllEmployees, createPayrollEntry, updateEmployeeRecord).
Business Logic: Any feature-specific logic, such as calculations for payroll or data transformations before sending or after receiving data from the API.
By keeping the API logic in service files, components and pages can remain clean and focused on presentation, while the services manage the data interactions.
