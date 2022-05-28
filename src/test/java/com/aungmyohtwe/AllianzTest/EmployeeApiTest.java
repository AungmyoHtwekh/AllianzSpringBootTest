package com.aungmyohtwe.AllianzTest;

import com.aungmyohtwe.AllianzTest.api.EmployeeApi;
import com.aungmyohtwe.AllianzTest.model.Employee;
import com.aungmyohtwe.AllianzTest.service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
public class EmployeeApiTest {

    @InjectMocks
    EmployeeApi employeeApi;

    @Autowired
    MockMvc mockMvc;

    @Mock
    EmployeeService employeeService;

    private Employee employee;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(employeeApi).build();
        employee = new Employee();
        employee.setName("aungaung");
        employee.setEmail("aungaung@gmail.com");
    }

    @Test
    public void saveEmployeeTest() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(employee );
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/employee/save")
                .content(requestJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    public void updateEmployeeTest() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        when(employeeService.findById((long) 1)).thenReturn(employee);
        String requestJson = ow.writeValueAsString(employee );
        this.mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/employee/update/1")
                .content(requestJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    public void deleteEmployeeTest() throws Exception {
        when(employeeService.findById((long) 1)).thenReturn(employee);
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/employee/delete/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void findEmployeeByIdTest() throws Exception {
        when(employeeService.findById((long) 1)).thenReturn(employee);
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/employee/get-employee/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
