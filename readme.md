# Spring PetClinic - Craft Demo

## Estimations

1. Capacity Calculations
    a. 8B world population
    b. USA population: 328 M
    c. 57% of world own pets: 4.56B
    d. 70% of USA owns pets: 230M
    e. Number of venerians in USA: ~100k
    f. # of pet visits per year average: ~1.5
    g. Total visits: ~350M
    h. # of visits/vetinerary: 350M/100k = 3500 per year (18 per day)
2. Data model:

    Owner
    Column Name	Data type	Size	Average
    id	INT(4)	4
    First_name	VARCHAR(30)	30
    Last_name	VARCHAR(30)	30
    address	VARCHAR(255)	255
    City	VARCHAR(80)	80
    telephone	VARCHAR(20)	20
    
    Total		 419B	400B
    
    vets			Average
    Column Name	Data type	Size
    id	INT(4)	4
    First_name	VARCHAR(30)	30
    Last_name	VARCHAR(30)	30
    Total		 64B	50B
    
    
    
    pets
    Column Name	Data type	Size	Average
    id	INT(4)	4
    name	VARCHAR(30)	30
    birth_date	DATE	4
    type_id	INT(4)	 4
    owner_id	INT(4)	4
    Total		46B	50B
    
    
    visits
    Column Name	Data type	Size	Average
    id	INT(4)	4
    Vet_id	INT(4)
    pet_id	INT(4)	4
    visit_date	DATE	4
    description	VARCHAR(255)	 255
    Total		271B	200B
    
    
    
    Total data for 1 year:
    
    Owners:
    400B * 230M = 100B bytes = 100GB
    
    Pets:
    50B * 230M = ~10 GB
    
    
    Vets:
    100K * 50Bytes = 50KB * 10 --> 500KB
    
    Visits:
    350M * 200B = ~100GB
    
    Total:
    210Gb --> Max 250 GB
    
    10% growth YoY
    Total data requirements:
    
    QPS:
    
    350M visits + New Owner creations ( 10% new owers/ year) + new vet creation ( negligible) + new pet creation ( 10% increase each year)
    
    25% cancellations
    ~500M upper limit / year = 1B queries/year upper limit
    
    1B/month = 400 requests/sec
    
    1B/year =  ~34request/sec --> approx 40requests/sec (not a lot)

## Change overview
    1. Add vet with one or multiple speciality.
    2. Cache eviction for cached vet information.
    3. Added notion of time slots for visit. Time slots are configurable.
    4. Added selection of Vet while adding visit.
    5. Added unique contraint  for a combination of vet_id,date,start_time.
    to ensure that Two pets cannot schedule for the same vet at the same time.
    6. Cancel visit functionality with hard delete of Visit record.
    7. Added lombok slf4j logging
    8. Added constraints validators for Visit dates.
    9. Fixed failing UTs due to new features.

## ToDo
    1. Configurable start and end time
    2. Configurable vet availablity days
    3. Add Unit tests for new features
    4. Add Load tests for new features
    5. Show visits for a selected vet

## Optimizations
    1. Introduce a status column in Visits to do soft deletes.
    2. Add a version column and a history table. Can be used for optimistic locks for better
    concurrency control.


## Scalability
    1. Introduce Redis to cache all Vets.
    2. Cache upcoming visits to faster lookups.
    3. Owner and Pet information can be cached with LRU eviction. 
    4. Add prometheus for monitoring system performance.
    5. Add ES logging for monitoring application metrics.
    6. Separate out UI and API pools so they can be scaled independently.
