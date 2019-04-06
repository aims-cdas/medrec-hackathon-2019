package medrechackathon.medlist.coordinator;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.hl7.fhir.dstu3.model.Base;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.Patient;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IClientInterceptor;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.interceptor.BasicAuthInterceptor;

public class EHR {
	private final String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
	private final String appConfigPath = rootPath + "../fhirpit.properties";

	String ehr1 = "po-ehr-1";
	String ehr2 = "po-ehr-2";
	String ehr3 = "po-ehr-3";
	String ehr4 = "OpenEMR";
	String serverBase1 = "https://fire-pit.mihin.org/po-ehr-1/baseDstu3";
	String username = "";
	String password = "";
	String serverBase2 = "https://fire-pit.mihin.org/po-ehr-2/baseDstu3";
	String serverBase3 = "https://fire-pit.mihin.org/po-ehr-3/baseDstu3";
	//String serverBase4 = "https://fire-pit.mihin.org/po-ehr-2/baseDstu3";	
	FhirContext ctx;
	DbConnector db;
	
	Properties props = new Properties();
	// Create an HTTP basic auth interceptor

	
	
	private void init() throws IOException{
		props.load(new FileInputStream(appConfigPath));	
		ctx = FhirContext.forDstu3();
		username = props.getProperty("fhir_pit_user");
		password = props.getProperty("fhir_pit_pw");
	}
	public EHR() throws IOException {
		init();
		db = new DbConnector();
		db.connect();
	}
	
	
	public int getPtIdFromCentralDb(String firstName, String lastName) throws Exception{

		int id = db.getPtIdFromCentralDb(firstName, lastName);
		return id;
	}
	
	public boolean checkForPtInCentral(String firstName, String lastName) throws Exception{
		DbConnector db = new DbConnector();
		db.connect();
		int count = db.getPtFromCentralDb(firstName, lastName);
		if (count == 1) {
			return true;
		} else if (count > 1) {
				throw new Exception("More than one patient with that name found");
		}

		return false;
	}
	
	
	public Patient getPtFromEhr1(String firstName, String lastName) {

		IClientInterceptor authInterceptor = new BasicAuthInterceptor(username, password);
		IGenericClient client = ctx.newRestfulGenericClient(serverBase1);
		client.registerInterceptor(authInterceptor);
		
		// Perform a search
		Bundle results = client
		      .search()
		      .forResource(Patient.class)
		      .where(Patient.GIVEN.matches().value(firstName))
		      .and(Patient.FAMILY.matches().value(lastName))
		      .returnBundle(Bundle.class)
		      .encodedJson()
		      .execute();
		Patient pt = new Patient();
		 if (results.getEntryFirstRep().getResource().getId() != null) {
		 pt = client
			      .read()
			      .resource(Patient.class)
			      .withId((results.getEntryFirstRep()).getResource().getId())
			      .execute();
		 }
		return pt;

	}
	public Patient getPtFromEhr2(String firstName, String lastName) {

		IClientInterceptor authInterceptor = new BasicAuthInterceptor(username, password);
		IGenericClient client = ctx.newRestfulGenericClient(serverBase2);
		client.registerInterceptor(authInterceptor);
		
		// Perform a search
		Bundle results = client
		      .search()
		      .forResource(Patient.class)
		      .where(Patient.GIVEN.matches().value(firstName))
		      .and(Patient.FAMILY.matches().value(lastName))
		      .returnBundle(Bundle.class)
		      .encodedJson()
		      .execute();
		 
		Patient pt = client
			      .read()
			      .resource(Patient.class)
			      .withId((results.getEntryFirstRep()).getResource().getId())
			      .execute();
		
		return pt;

	}
	public Patient getPtFromEhr3(String firstName, String lastName) {

		IClientInterceptor authInterceptor = new BasicAuthInterceptor(username, password);
		IGenericClient client = ctx.newRestfulGenericClient(serverBase3);
		client.registerInterceptor(authInterceptor);
		
		// Perform a search
		Bundle results = client
		      .search()
		      .forResource(Patient.class)
		      .where(Patient.GIVEN.matches().value(firstName))
		      .and(Patient.FAMILY.matches().value(lastName))
		      .returnBundle(Bundle.class)
		      .encodedJson()
		      .execute();
		 
		Patient pt = client
			      .read()
			      .resource(Patient.class)
			      .withId((results.getEntryFirstRep()).getResource().getId())
			      .execute();
		
		return pt;

	}
	public Patient getPtFromRemote(String firstName, String lastName) throws Exception {
		Patient pt1 = getPtFromEhr1(firstName, lastName);
		if (pt1 != null) {
			if(checkForPtInCentral(firstName,lastName)) {
				int id = getPtIdFromCentralDb(firstName,lastName);
				if (!checkForPtSource(id,ehr1)) {
					insertPtSource(id, ehr1, pt1, serverBase1);
				}
			}
			
		}
		/*Patient pt2 = getPtFromEhr2(firstName, lastName);
		if (pt2 != null) {
			if(checkForPtInCentral(firstName,lastName)) {
				int id = getPtIdFromCentralDb(firstName,lastName);
				if (!checkForPtSource(id,ehr2)) {
					insertPtSource(id, ehr2, pt2, serverBase2);
				}
			}
		}
		Patient pt3 = getPtFromEhr3(firstName, lastName);
		if (pt3 != null) {
			if(checkForPtInCentral(firstName,lastName)) {
				int id = getPtIdFromCentralDb(firstName,lastName);
				if (!checkForPtSource(id,ehr3)) {
					insertPtSource(id, ehr3, pt3, serverBase3);
				}
			}
		}*/
			//todo - add step to truly compare all return results - using gold standard for today
		return pt1;
	}
	
	private boolean checkForPtSource(int id, String ehr) {
		// TODO Auto-generated method stub
		int count = db.checkPtSource(id,ehr);
		if (count == 1) {
			return true;
		}
		return false;
	}
	private void insertPtSource (int id, String ehr, Patient pt, String serverBase) {
			db.insertPtSource(id, ehr, pt, serverBase);
	}
	public static void main(String[] args) throws Exception {

		EHR ehr = new EHR();
		
		if (ehr.checkForPtInCentral("Millie", "Bryant")) {
			//ehr.getMedListFromEhrs();
		} else {
			ehr.getPtFromRemote("Millie", "Bryant");
		}
		
		if (ehr.checkForPtInCentral("George", "Tullison")) {
			//ehr.getMedListFromEhrs();
		} else {
			ehr.getPtFromRemote("George", "Tullison");
		}
		
		if (ehr.checkForPtInCentral("Sarah", "Thompson")) {
			//ehr.getMedListFromEhrs();
		} else {
			ehr.getPtFromRemote("Sarah", "Thompson");
		}
		
		if (ehr.checkForPtInCentral("Christie", "Munson")) {
			//ehr.getMedListFromEhrs();
		} else {
			ehr.getPtFromRemote("Christie", "Munson");
		}
		
		if (ehr.checkForPtInCentral("Alex", "Gonzalez")) {
			//ehr.getMedListFromEhrs();
		} else {
			ehr.getPtFromRemote("Alex", "Gonzalez");
		}
		
		if (ehr.checkForPtInCentral("Santiago", "Morales")) {
			//ehr.getMedListFromEhrs();
		} else {
			ehr.getPtFromRemote("Santiago", "Morales");
		}
	//System.out.println(ctx.newJsonParser().setPrettyPrint(true).encodeResourceToString(results));
	}
}
