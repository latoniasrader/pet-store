        package pet.store.service;
       import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pet.store.controller.model.PetStoreData;
import pet.store.controller.model.PetStoreData.PetStoreCustomer;
import pet.store.controller.model.PetStoreData.PetStoreEmployee;
import pet.store.dao.CustomerDao;
import pet.store.dao.EmployeeDao;
import pet.store.dao.PetStoreDao;
import pet.store.entity.Customer;
import pet.store.entity.Employee;
import pet.store.entity.PetStore;

        @Service
        public class PetStoreService {
        	
	    @Autowired
	    private   PetStoreDao petStoreDao;
	    
        @Transactional(readOnly = false)
	    public PetStoreData savePetStore(PetStoreData petStoreData) {
		Long PetStoreId = petStoreData.getPetStoreId();
		PetStore petStore = findOrCreatePetStoreById(PetStoreId);
		
	    copyPetStoreField(petStore, petStoreData);
	    return new PetStoreData(petStoreDao.save(petStore));
        }
	
		private void copyPetStoreField(PetStore petStore, PetStoreData petStoreData) {
	    petStore.setPetStoreName(petStoreData.getPetStoreName());
		petStore.setPetStoreAddress(petStoreData.getPetStoreAddress());
	    petStore.setPetStoreCity(petStoreData.getPetStoreCity());
        petStore.setPetStoreState(petStoreData.getPetStoreState());
        petStore.setPetStoreZip(petStoreData.getPetStoreZip());
        petStore.setPetStorePhone(petStoreData.getPetStorePhone());
		}
        private PetStore findOrCreatePetStoreById(Long petStoreId) {
        if(Objects.isNull(petStoreId)) {
		return new PetStore();
		}
		else {
		return findPetStoreById(petStoreId);
		}
		
	    }
		private PetStore findPetStoreById(Long petStoreId) {
		return petStoreDao.findById(petStoreId)
	    .orElseThrow(()-> new NoSuchElementException(
		"Pet store with ID=" + petStoreId + " was not found."));
		}
        @Transactional(readOnly = true)
		public List<PetStoreData> retrieveAllPetStore() {
		List<PetStore> petStores = petStoreDao.findAll();
		List<PetStoreData> result = new LinkedList<>();
		
		for(PetStore petStore : petStores) {
		PetStoreData psd = new PetStoreData(petStore);
			
		psd.getCustomers().clear();
		psd.getEmployees().clear();
			
		result.add(psd);
		}
		return result;
		
        }
        public PetStoreData retrievePetStoreById(Long petStoreId) {
		PetStore petStore = findPetStoreById(petStoreId);
		return new PetStoreData(petStore);
		}
      
        
		
		@Autowired
		private EmployeeDao employeeDao;
		
		@Transactional(readOnly = false)
		public PetStoreEmployee saveEmployee(Long petStoreId, PetStoreEmployee petStoreEmployee) {
		PetStore petStore = findOrCreatePetStoreById(petStoreId);
		Long employeeId = petStoreEmployee.getEmployeeId();
		Employee employee = findOrCreateEmployee(petStoreId, employeeId);
			
		copyEmployeeFields(employee,petStoreEmployee);
		employee.setPetStore(petStore);
		petStore.getEmployees().add(employee);
		Employee dbEmployee =  employeeDao.save(employee);
		return new PetStoreEmployee(dbEmployee);
		}

		private void copyEmployeeFields(Employee employee, PetStoreEmployee petStoreEmployee) {
		employee.setEmployeeId(petStoreEmployee.getEmployeeId());
		employee.setEmployeeFirstName(petStoreEmployee.getEmployeeFirstName());
	    employee.setEmployeeLastName(petStoreEmployee.getEmployeeLastName());
	    employee.setEmployeePhone(petStoreEmployee.getEmployeePhone());
        employee.setEmployeeJobTitle(petStoreEmployee.getEmployeeJobTitle());
			
		}

		private Employee findOrCreateEmployee(Long petStoreId, Long employeeId) {
		if(Objects.isNull(employeeId)) {
		return new Employee();	
		}
		else {
		return findEmployeeId(petStoreId, employeeId);
		}
			
		}

		private Employee findEmployeeId(Long petStoreId, Long employeeId) {
		Employee employee = employeeDao.findById(employeeId).orElseThrow();
		if(employee.getPetStore().getPetStoreId()!= petStoreId) {
			    throw new IllegalArgumentException("The employee with ID=" + employeeId + 
			    		" is not employeed by the pet store with ID=" + petStoreId + ".");
			}

			return employee;	
		
		
		
		
		}
		@Autowired
		private CustomerDao customerDao;
		
		@Transactional(readOnly = false)
		public PetStoreCustomer saveCustomer(Long petStoreId, PetStoreCustomer petStoreCustomer) {
		PetStore petStore = findOrCreatePetStoreById(petStoreId);
		Long customerId = petStoreCustomer.getCustomerId();
		Customer customer = findOrCreateCustomer(petStoreId, customerId);
			
		copyCustomerFields(customer,petStoreCustomer);
		customer.getPetStores().add(petStore);
		petStore.getCustomers().add(customer);
		Customer dbCustomer =  customerDao.save(customer);
		return new PetStoreCustomer(dbCustomer);	
			
		}

		private void copyCustomerFields(Customer customer, PetStoreCustomer petStoreCustomer) {
		customer.setCustomerId(petStoreCustomer.getCustomerId());
		customer.setCustomerFirstName(petStoreCustomer.getCustomerFirstName());
		customer.setCustomerLastName(petStoreCustomer.getCustomerLastName());
		customer.setCustomerEmail(petStoreCustomer.getCustomerEmail());
			
		}

		private Customer findOrCreateCustomer(Long petStoreId, Long customerId) {
		if(Objects.isNull(customerId)) {
		return new Customer();	
		}
		else {
		return findCustomerId(petStoreId, customerId);
		}
		
		}

		private Customer findCustomerId(Long petStoreId, Long customerId) {
		Customer customer = customerDao.findById(customerId).orElseThrow(()-> new NoSuchElementException(
				"customer with ID=" + customerId + "was not found."));
		  
		boolean found = false;
		
		for(PetStore petStore : customer.getPetStores()) {
			if(petStore.getPetStoreId() == petStoreId) {
				
			found = true;
			break;
			}

	}
		if(!found) {
			
	throw new IllegalArgumentException("The customer with ID=" + customerId + 
			" is not a member of the pet store with ID=" + petStoreId + ".");
	}
		return customer;	
		}
			
		
        @Transactional(readOnly = false)
		public void deletePetStoreById(Long petStoreId) {
	    PetStore petStore = findPetStoreById(petStoreId);
	    petStoreDao.delete(petStore);
			
		}

		

		}

		
		
        
        
        
        
        
				
				
				


