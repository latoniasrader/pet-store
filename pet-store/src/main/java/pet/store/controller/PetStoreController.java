    package pet.store.controller;

    import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import pet.store.controller.model.PetStoreData;
import pet.store.controller.model.PetStoreData.PetStoreCustomer;
import pet.store.controller.model.PetStoreData.PetStoreEmployee;
import pet.store.service.PetStoreService;

    @RestController
    @RequestMapping("/pet_store")
    @Slf4j
    public class PetStoreController {
	@Autowired
	private PetStoreService petStoreService;
	
	@PostMapping("/petStore")
	@ResponseStatus(code = HttpStatus.CREATED)
	public PetStoreData insertPetStore(@RequestBody PetStoreData petStoreData) {
	log.info("This is a log line {}", petStoreData);	
    return petStoreService.savePetStore(petStoreData);
	}
	@PutMapping("/petStore/{petStoreId}")
	public PetStoreData updatePetStore(@PathVariable Long petStoreId,
	@RequestBody PetStoreData petStoreData) {
	petStoreData.setPetStoreId(petStoreId);
	log.info("Updating petStore {}", petStoreData);
	return petStoreService.savePetStore(petStoreData);
	}
	@GetMapping("/petStore")
	public List<PetStoreData> retrieveAllPetStore(){
	log.info("Retrieve all petStore call.");
	return petStoreService.retrieveAllPetStore();
	}
	@GetMapping("/petStore/{petStoreId}")
	public PetStoreData retrievePetStoreById(@PathVariable Long petStoreId) {
	log.info("Retrieving petStore with ID= {}",petStoreId);
	return petStoreService.retrievePetStoreById(petStoreId);
	}
	@DeleteMapping("/petStore/{petStoreId}")
    public Map<String,String> deletePetStoreById(@PathVariable Long petStoreId){
    log.info("Deleting petStore with ID= {}", petStoreId);
    petStoreService.deletePetStoreById(petStoreId);
    return Map.of("message", "Deletion of petStore with ID= was successful." + petStoreId);
    }
	
	
    
	@PostMapping("/pet_store/{petStoreId}/employee")
	@ResponseStatus(code = HttpStatus.CREATED)
	public PetStoreEmployee insertEmployee(@PathVariable Long employeeId, 
	@RequestBody PetStoreEmployee petStoreEmployee) {
	petStoreEmployee.setEmployeeId(employeeId);
	log.info("Creating employee {}", petStoreEmployee);
	return petStoreService.saveEmployee(employeeId, petStoreEmployee);
	}
	@PostMapping("/pet_store/{petStoreId}/customer")
	@ResponseStatus(code = HttpStatus.CREATED)
	public PetStoreCustomer insertCustomer(@PathVariable Long customerId, 
	@RequestBody PetStoreCustomer petStoreCustomer) {
	petStoreCustomer.setCustomerId (customerId);	
	log.info("Creating customer {}", petStoreCustomer);
	return petStoreService.saveCustomer(customerId, petStoreCustomer);
	}
	

}

