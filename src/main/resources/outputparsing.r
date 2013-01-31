# required packages are preloaded and defined here:/Library/Frameworks/R.framework/Versions/2.15/Resources/library/base/R/Rprofile
#library(maptools)   # for geospatial services; also loads foreign and sp
#library(rgdal)      # for map projection work; also loads sp
#library(rgeos)
#library(gdata)
#library(RJSONIO)

f_outputparsing <- function(){
  if(length(gRltList) == 0) return
  
  # return a list containing elements in geojson/json format
  RltJSONList = list()
  for(i in 1: length(gRltList)){
    # transtlate into geojson for each element (dataframe) in gRltList
    print(sprintf("=== parsing gRltList[%i]",i))
    RltJSONList[[i]] = f_DataFrame2JSONString(gRltList[[i]])
  }
    
  return(RltJSONList)
}

f_DataFrame2JSONString <- function(targetDataFrame=NULL)
{
  if(is.null(targetDataFrame)) return("[]")
  
  if(nrow(targetDataFrame)==0) return("[]")
  
  tmpFilePath = "./dump.geojson"
  tmpStr = ""
  # for spatial data frame, convert to geojson
  if(inherits(targetDataFrame, "Spatial")){
    print("=== spatial dataframe detected")
    if(file.exists(tmpFilePath)) file.remove(tmpFilePath)   
    writeOGR(obj=targetDataFrame, dsn=tmpFilePath, layer="dump", driver = "GeoJSON",  check_exists=TRUE, overwrite_layer=TRUE)    
    tmpStr = readChar(tmpFilePath, file.info(tmpFilePath)$size)
    return(tmpStr)
  }
  
  # for normal data frame, convert to json
  print("=== non-spatial dataframe detected")
  vec=c()
  for(i in 1:nrow(targetDataFrame)){
    vec[i] = toJSON(targetDataFrame[i,], collapse="")
  }
  tmpStr = paste(vec,collapse=",")
  tmpStr = paste("[",tmpStr,"]",sep="")
  return(tmpStr)
  
}


f_outputparsing()