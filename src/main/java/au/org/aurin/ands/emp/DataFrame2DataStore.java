package au.org.aurin.ands.emp;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import oms3.annotations.Author;
import oms3.annotations.Description;
import oms3.annotations.Execute;
import oms3.annotations.In;
import oms3.annotations.Keywords;
import oms3.annotations.License;
import oms3.annotations.Name;
import oms3.annotations.Out;

import org.apache.commons.io.IOUtils;
import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPMismatchException;

import org.rosuda.REngine.RList;
import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import au.edu.uq.preload.LoadRScriptEmpcluster;
import au.org.aurin.security.util.SslUtil;


public class DataFrame2DataStore {
	@In
	public RConnection cIn;
	
	@In
  public String xAurinUserId;
	
	@In
  public String pathToTmpData;
	
	@Out
  public String urlInDataStore;
	
	@Description("REXP result complex object")
	@Out
	public REXP worker;

	
	@Execute
	public void exec() throws RserveException{
		try {
			this.cIn.assign("script", LoadRScriptEmpcluster.getDataFrame2JSONScript());
			this.worker = this.cIn.eval("try(eval(parse(text=script)),silent=FALSE)");
			
			if(this.worker == null){
				System.out.println("==== worker init failed");
			}
			
			this.setupOutputs();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
    
		
    try {
        FileInputStream inputStream = new FileInputStream(pathToTmpData);
        String tmpDataFileString = IOUtils.toString(inputStream);
        inputStream.close();
        
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
        //headers.add("Accept", "application/json");
        headers.add("Content-Type", "application/json");
        headers.add("X-AURIN-USER-ID", this.xAurinUserId);
        
        RestTemplate restTemplate = new RestTemplate();
        //HttpHeaders headers = new HttpHeaders();
        //headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<String>(tmpDataFileString, headers);
        SslUtil.trustJavaTrustStore();
        urlInDataStore = restTemplate.postForObject("https://dev-api.aurin.org.au/mservices"
	           + "/datastore/store/" + this.xAurinUserId, requestEntity, String.class);
    } catch (FileNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } 

	}
	
	private void setupOutputs() {

		RList resultL = null;

		try {
			if (!this.worker.isNull()) {
				System.out.println(" We have results back from R");

				if (this.worker.inherits("try-error") || this.worker.isString()) {
					throw new REXPMismatchException(this.worker,
							"Try-Error from R \n" + this.worker.toString());
				} else if (this.worker.isList()) {
					// result list reply
					resultL = this.worker.asList();
					System.out.println("resultL.size() = " + resultL.size());
					for (int i = 0; i < resultL.size(); i++) {
						//System.out.println("r" + i + " = " + resultL.at(i).asString());
					}

					System.out.println("========= test end =========");

				}
			}
		} catch (REXPMismatchException me) {
			me.printStackTrace();
		}

	}
}

