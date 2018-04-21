package ch.puzzle.marinabackend.employee;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.hateoas.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import ch.puzzle.marinabackend.MarinaBackendApplication;
import ch.puzzle.marinabackend.TestConfiguration;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { MarinaBackendApplication.class,
		TestConfiguration.class }, webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Transactional
public class EmployeeResourceDataTest {
	
	@Autowired
	private EntityManager entityManager;

	@Autowired
	private EmployeeRepository employeRepository;
	
	@Autowired
    private EmployeeResource employeeResource;


	@Test
	public void shouldFindAllEmployees() throws Exception {
	    //given
		Employee employee = new Employee();
		employee.setFirstName("Housi");
		employee.setLastName("Mousi");
		employee.setEmail("housi.mousi@marina.ch");
		employee.setUsername("hmousi");
		employee.setBruttoSalary(BigDecimal.valueOf(1000.45));
		entityManager.persist(employee);
	    entityManager.flush();

	    //when
	    Iterable<Employee> employees = employeRepository.findAll();

	    //then
	    Employee result = employees.iterator().next();
	    assertEquals(employee, result);
	    assertNotNull(result.getCreatedDate());
	    assertNotNull(result.getModifiedDate());
	}
	
	@Test
    public void shouldFindEmployeeByEmail() throws Exception {
        //given
        Employee employee = new Employee();
        employee.setFirstName("Housi");
        employee.setLastName("Mousi");
        employee.setEmail("housi.mousi@marina.ch");
        employee.setUsername("hmousi");
        employee.setBruttoSalary(BigDecimal.valueOf(1000.45));
        entityManager.persist(employee);
        entityManager.flush();

        //when
        ResponseEntity<Resource<Employee>> result = employeeResource.getEmployeeByEmail("housi.mousi@marina.ch");

        //then
        assertNotNull(result);
        assertEquals(employee, result.getBody().getContent());
        
	}
	
	@Test
    public void shouldNotFindEmployeeByEmail() throws Exception {
        //given
        Employee employee = new Employee();
        employee.setFirstName("Housi");
        employee.setLastName("Mousi");
        employee.setEmail("housi.mousi@marina.ch");
        employee.setUsername("hmousi");
        employee.setBruttoSalary(BigDecimal.valueOf(1000.45));
        entityManager.persist(employee);
        entityManager.flush();

        //when
        ResponseEntity<Resource<Employee>> result = employeeResource.getEmployeeByEmail("not.housi.mousi@marina.ch");

        //then
        assertEquals(ResponseEntity.notFound().build(), result);
        
    }
        

}
