 
#### What assumptions did you make?

 - We will be directly serving from 3rd party API (with no stale data issue in any case provided 3rd party data is single source of truth).
 - product/API should be backward compatible for easy transition. Hence, focus on making the same signature APIs.
   (expect for response DTOs which are slightly modified, with no loss of data)
 - Assumed that public key will be liable to be changed in future. So, calling every time export meters request is made.
 - clients(engineers or programs) should not need any credentials (for initial version) to use newly created API.

#### Which part was the most difficult, and how did you get unstuck?

 ##### 2 parts were most difficult in terms of that they took too much time of mine.
     1. I have done login to Urja legacy API programmatically, at the time of whenever there is no cookie in the application session or the cookie is expired.
        Major challenge was calling that API, got to various things like that 
         - it is not a JSON body based submission.
         - most importantly, it was redirecting the response. so that was getting hard to extract the cookie out of it. so had to disable that by configuring 
         the internal used calling client (rest template).

     2. I needed to figure out how x-signature and x-timestamp were being computed out of a public signing key. For that, I need to scan the FE code. Finally got a format by which message 
     for encryption algorithm was constructed (with various parameters). and finally implemented that same into my code. using the same algorithm required googling.

    Apart from it, my initial time went into the end to end setup of the application + MySQL DB. I was planning to use the DB for miscellaneous purposes.
    Hence kept opened the scope of it. 

#### If you had another day, what would you improve?

  - Login/Logout for enginners/programs. Metrics for API usage user wise. Implementing metrics/logs.
  - More fancy operations(APIs) like nearest meters, nearest transformers, and meters under a transformer etc.
  - a simple UI. 


#### What mistake did you make while solving this (there's always one)?

  - Didn't looked at login response initially, that how it could impact when done programmatically. (due to redirects).
  

#### If you were reviewing your own submission, what would you criticise?

 - There is no UI. At least anyone expects a UI. They are humans not robos to hard stare on swagger ui :)
 
