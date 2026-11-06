How to run:





### Assumptions:

- We will be serving the latest data to the client, without any possibility of serving stale data; for this purpose data is being returned to the client after processed 

directly from the original source (Having the latest data). This assumption is made due to assuming a critical need of the product. 

 Future Scope: Data can be reconciled every 24 hours via a cron job to serve directly from application DB instead of relying on third party APIs for every call everytime. 

- Client will be making / seeking the exact response type of the data as he is currently getting from legacy portals. Hence, for smooth transition request and 
response structure are kept same as of legacy portal.

- No assumption is made with public signing key. It is assumed that it is liable to be changed in the future, hence kept the development while keeping this in mind.

### Design Decision & Trade-offs:

- Using Adapter Pattern to accommodate for any upcoming another third party API to retrieve the same set of data.
- Using modular monolith (a separate module left for auth service -- to make call to our new API gated with login wall).
- While DB call can result into faster response reducing the latency and network overhead, it is traded off right now to use 3rd party API directly for latest data.

### What I intentionally left out:


### What I'd improve or do differently with more time :

- Add a auth service. that will facilitate in keeping track of 'who call--which request'. That will help in giving expirable api-keys for programmatic access. Also, leastly user management (engineers who will be using the new portal). 

- Build more fancy APIs. For example, to search nearest meters, nearest transformers, all meters under some particular transformer(using its unique key).

- A nice clean UI to display data in revealing form. (To quickly draw out information/conclusions out of it).
  
- Add observability(logs and metrics), rate limiting.

- Write unit and integration tests for all modules.

- Retry mechanism to call 3rd party APIs with global error handling.

- multiple export format for the meters data. (JSON, CSV)




Find my reflection on the project by [clicking here.](/REFLECTION.md) or navigating to REFLECTION.md yourself.

Find my initial scanning of the portal to get to know what I have to build: [click here](/PROTOCOL.md) or navigate to PROTOCOL.md

Find the final API contract (OpenAPI 3.x) listing all APIs and the schema of responses: [click here](/openapi.json).








 
