
The 10 digit code in the ABS Journey to Work tables represents:
1st digit = state code
2nd digit = study area code (eg detailed study area = 1, extended study area = 2)
3 to 6 digits = 4 digit SLA code
7 to 10 digits =dzn code

The DZN code will be required to link to associated GIS fields (eg. Vicdznp06).

---------------------------------------------

In this version, small island zones have been merged with the adjoining MSD mainland zones (where the transport zones match). This deduplication has not been applied to areas outside of the MSD, so duplicate zones may exist.

The JTW boundary file should contain 1949 DZNs in the MSD.


---------------------------------------------

>>When using ABS data to a DZN level:

As well as mapped zones, there may be reference to several ranges of "dump zones".

3000 to 3999 range - Locality given but no Street specified

This series of codes may arise when a locality spreads across more than one zone but the response does not quote a needed street at all so that the correct zone within the locality can be chosen. Alternatively a street or facility (eg hospital or major business) was given but could not be found on the JTW zonal index or with other resources available to the Census Data Processing Centre (DPC) during coding. A special case of this is 9985 for Melbourne with not enough further information to allocate to an on ground zone. We can interpret these responses as being somewhere in the locality specified but really as reflected by the working public's usage of that locality name rather than the locality gazetted boundaries. Where the usage of a locality name differs from the official boundaries then there is more scope for the response to be actually outside the official locality. Examples of usage differing known to us are usage of Broadmeadows extending into Campbellfield (eg Ford Factory on Hume Highway Campbellfield commonly called Broadmeadows) and usage of Altona extending into Altona North (eg Toyota on Grieve Pde Altona North commonly called Altona). We have tried to make our JTW index reflect most of these known extensions of usage by extending official locality boundaries so that better zonal matching will occur. However the unknown usage may cause problems and fall into the 3000 to 3999 series of codes. 

Where large numbers of these 3000 series responses have occurred we have avoided putting them into one of the possible on-ground zones as this will distort the zonal employment picture. We take the view that users of the data can apply their own allocation systems to these unknowns on the basis of other information using the allocation method that satisfies them.


4000 to 4999 Zones - No Street number given for major employment streets.

This series of codes may arise when a street with intensive employment within a locality spreads across substantially more than one zone but the response does not quote a needed street number at all so that the correct zone within the locality can be easily chosen. Alternatively the  facility information if given (eg hospital or major businesses located on this street) could not be found on the thousands of known facilities included in the JTW zonal index or with other resources available to the ABS DPC during coding. 

Where large numbers of these 4000 series responses have occurred we have avoided putting them into one of the possible on-ground zones as this will distort the zonal employment picture. We take the view that users of the data can apply their own allocation systems to these unknowns on the basis of other information using the allocation method that satisfies them.


