package com.binarydiff.binaryDiff;

import com.binarydiff.binaryDiff.repositories.LeftItemRepository;
import com.binarydiff.binaryDiff.repositories.RightItemRepository;
import com.binarydiff.binaryDiff.services.DiffService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureDataJpa
class BinaryDiffApplicationTests {

	@Autowired
	MockMvc mockMvc;

	@Test
	void createLeftItemIntegrationTest() throws Exception {
		mockMvc.perform(post("/v1/diff/1/left")
				.contentType("application/json")
				.content("eyJtZXNzYWdlIiA6ICJIZWxsbyB3b3JsZCEifQ=="))
				.andExpect(status().isCreated());
	}

	@Test
	void createRightItemIntegrationTest() throws Exception {
		mockMvc.perform(post("/v1/diff/1/right")
				.contentType("application/json")
				.content("eyJtZXNzYWdlIiA6ICJIZWxsbyB3b3JsZCEifQ=="))
				.andExpect(status().isCreated());
	}

	@Test
	void createTwoEqualItemsAndGetDiffs() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		createLeftAndRightItems("eyJtZXNzYWdlIiA6ICJIZWxsbyB3b3JsZCEifQ==", "eyJtZXNzYWdlIiA6ICJIZWxsbyB3b3JsZCEifQ==", 1);
		MvcResult result = mockMvc.perform(get("/v1/diff/1")
				.contentType("application/json"))
				.andExpect(status().isOk()).andReturn();
		Assertions.assertEquals(mapper.readTree("{\"message\" : \"Hello world!\"}"), mapper.readTree(result.getResponse().getContentAsString()));
	}

	@Test
	void createTwoDifferentItemsAndGetDiffs() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		createLeftAndRightItems("eyJtZXNzYWdlIiA6ICJIZWxsbyB3b3JsZCEifQ==", "eyJtZXNzYWdlIiA6ICJHb29kYnllIHdvcmxkISJ9", 1);
		MvcResult result = mockMvc.perform(get("/v1/diff/1")
				.contentType("application/json"))
				.andExpect(status().isOk()).andReturn();
		Assertions.assertEquals(mapper.readTree("{\"message\" : \"(Hello world!, Goodbye world!)\"}"), mapper.readTree(result.getResponse().getContentAsString()));
	}

	@Test
	void createInvalidJSonLeftObject() throws Exception{
		mockMvc.perform(post("/v1/diff/1/left")
				.contentType("application/json")
				.content("SGVsbG8gV29ybGQh"))
				.andExpect(status().isNotAcceptable());
	}

	@Test
	void createInvalidJSonRightObject() throws Exception{
		mockMvc.perform(post("/v1/diff/1/right")
				.contentType("application/json")
				.content("SGVsbG8gV29ybGQh"))
				.andExpect(status().isNotAcceptable());
	}

	@Test
	void createLargerRightMessageAndGetDiffs() throws Exception{
		ObjectMapper mapper = new ObjectMapper();
		createLeftAndRightItems("eyJtZXNzYWdlIiA6ICJIZWxsbyBXb3JsZCEifQ==",
				"eyJtZXNzYWdlIiA6ICJIZWxsbyBXb3JsZCEiLAogICAgInNlY29uZE1lc3NhZ2UiOiAiSGF2ZSBhIG5pY2UgZGF5LiIKfQ==",
				1);
		MvcResult result = mockMvc.perform(get("/v1/diff/1")
				.contentType("application/json"))
				.andExpect(status().isOk()).andReturn();
		Assertions.assertEquals(mapper.readTree("{\"secondMessage\" : \"Have a nice day.\"}"), mapper.readTree(result.getResponse().getContentAsString()));

	}

	@Test
	void tryToGetDifferencesOfUnexistentItems() throws Exception{
		mockMvc.perform(get("/v1/diff/2")
				.contentType("application/json"))
				.andExpect(status().isNotFound());
	}

	private void createLeftAndRightItems(String leftString, String rightString, Integer id) throws Exception {
		mockMvc.perform(post("/v1/diff/" + id + "/right")
				.contentType("application/json")
				.content(rightString))
				.andExpect(status().isCreated()).andReturn();
		mockMvc.perform(post("/v1/diff/" + id + "/left")
				.contentType("application/json")
				.content(leftString))
				.andExpect(status().isCreated()).andReturn();
	}

}
