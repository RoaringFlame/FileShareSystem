package fss.web;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

import org.junit.Test;
import org.springframework.test.web.servlet.MockMvc;

public class UserControllerTest {

  //this test is unusable ,just a sample to show how to use mock test
  @Test
  public void testHomePage() throws Exception {
    MockMvc mockMvc = standaloneSetup().build();
    mockMvc.perform(get("/"))
           .andExpect(view().name("index"));
  }

}
