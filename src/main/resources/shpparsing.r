# required packages
library(maptools)   # for geospatial services; also loads foreign and sp
library(gpclib)     # General Polygon Clipping library 
library(rgdal)      # for map projection work; also loads sp
library(rgeos)
library(PBSmapping) # for GIS_like geospatial object manipulation / anslysis including poly
gpclibPermit()
require(gpclib)

#set up the working directory
#setwd("/Users/Shared/Documents/AURIN/R")
CONST_projected_proj4string = "+proj=merc +datum=WGS84"
testData = NULL
testPolyList = NULL

f_shpparsing <- function(){
  print(sprintf("======== shpURL :%s", shpUrl))
  
#  ogrInfo(dsn=shpUrl,layer = 'OGRGeoJSON')
#  x<-readOGR(dsn=shpUrl, layer = tmpRlt)
  
  x <- readShapePoly(shpUrl)
  attr(x@proj4string,"projargs") = "+proj=longlat +ellps=GRS80 +no_defs"
  original_proj4string = attr(x@proj4string,"projargs")
  
  # check if the original data is projected
  # Transform the polygons (which were read in as unprojected geographic coordinates) to an Albers Equal Area projection
  # this is essential since we need calculate distance/area for polygons
  if (is.projected(x)){
    x_pj = x
  } else
  {
    x_pj = spTransform(x,CRS(CONST_projected_proj4string))
  }
  
  
  DATA_ROW_NUM = nrow(x_pj@data)
  displayColNames = c("LGA_CODE", "LGA", "ZONE_CODE", "X2310", "X2412", "X8500")
  testData <<- x_pj@data[1:DATA_ROW_NUM, displayColNames]
  testData[,"wardclut"] = 1:DATA_ROW_NUM
  testPolyList <<- x_pj@polygons[1:DATA_ROW_NUM]
  print(sprintf("=====================load data rows:%i", nrow(testData)))
}

f_shpparsing()