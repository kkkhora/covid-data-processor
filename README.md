# covid-data-processor

This program computes covid data from all states of United States from 2021-3-25 to 2021-11-05.
Data Structures include: TreeMap, LinkedList, ArrayList, Array

<img width="513" alt="Screenshot 2022-07-27 at 15 22 29" src="https://user-images.githubusercontent.com/76552190/181188909-9efdc275-2b11-4a60-8597-b47fe72ab96b.png">

About computation 6 factor:
The additional feature measures the factor of the willingness of local people to be fully vaccinated against COVID. 
We assume that the fully-vaccinated number is affected by two factors: a.) the economic status of a person, in our case represented by the livable area per capita; b.) the willingness to be fully vaccinated. 
Specifically, the factor is calculated based on the total fully-vaccinated number (covid_data file) per capita(population.txt) being divided by the total livable area(properties.csv) per capita via Zip Code. 
By dividing the livable area per capita, we rule out the economic factor (the objective factor), thus leaving only the subjective factor, which is the willingness of local people to be fully vaccinated.
