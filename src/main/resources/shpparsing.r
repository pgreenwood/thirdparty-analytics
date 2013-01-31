# required packages are preloaded and defined here:/Library/Frameworks/R.framework/Versions/2.15/Resources/library/base/R/Rprofile
#library(maptools)   # for geospatial services; also loads foreign and sp
#library(gpclib)     # General Polygon Clipping library 
#library(rgdal)      # for map projection work; also loads sp
#library(rgeos)
#library(PBSmapping) # for GIS_like geospatial object manipulation / anslysis including poly
gpclibPermit()
#require(gpclib)


CONST_projected_proj4string = "+proj=merc +datum=WGS84"
gAttrData = NULL
gPolyData = NULL
gRltList = list()
gOriginalProj4string = ""
gIgnoreEmptyRowJobNum = 10

f_shpparsing <- function(){
  #print(sprintf("======== shpURL :%s", shpUrl))
  x <- readShapePoly(shpUrl)
  attr(x@proj4string,"projargs") = "+proj=longlat +ellps=GRS80 +no_defs"
  gOriginalProj4string <<- attr(x@proj4string,"projargs")
  
  # check if the original data is projected
  # Transform the polygons (which were read in as unprojected geographic coordinates) to an Albers Equal Area projection
  # this is essential since we need calculate distance/area for polygons
  if (is.projected(x)){
    x_pj = x
  } else
  {
    x_pj = spTransform(x,CRS(CONST_projected_proj4string))
  }
  
  gAttrData <<- x_pj@data
  gPolyData <<- x_pj@polygons
  #print(sprintf("=====================load data rows:%i", nrow(gAttrData)))
}

f_shpparsing()