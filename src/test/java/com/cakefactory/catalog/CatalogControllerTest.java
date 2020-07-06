package com.cakefactory.catalog;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.util.Collections;

import com.cakefactory.basket.Basket;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.htmlunit.MockMvcWebClientBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(controllers = CatalogController.class)
class CatalogControllerTest {

	private WebClient webClient;

	@Autowired
	MockMvc mockMvc;

	@MockBean
	CatalogService catalogService;

	@MockBean
	Basket basket;

	@BeforeEach
	void setUp() {
		this.webClient = MockMvcWebClientBuilder.mockMvcSetup(mockMvc).build();
	}

	@Test
	@DisplayName("index page returns the landing page")
	void returnsLandingPage() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/")).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("Cake Factory")));
	}

	@Test
	@DisplayName("index page return a list of items from the database")
	void returnsListOfItemsFromDb() throws Exception {
		final String expectedTitle = "Red Velvet";
		mockItems(expectedTitle, BigDecimal.valueOf(3));

		HtmlPage page = webClient.getPage("http://localhost/");

		assertThat(page.querySelectorAll(".item-title"))
				.anyMatch(domElement -> expectedTitle.equals(domElement.asText()));
	}

	@Test
	@DisplayName("index page displays number of items in basket")
	void displaysNumberOfItems() throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		when(basket.getTotalItems()).thenReturn(3);

		HtmlPage page = webClient.getPage("http://localhost/");

		DomNode totalElement = page.querySelector(".basket-total");
		assertThat(totalElement).isNotNull();
		assertThat(totalElement.asText()).isEqualTo("3");
	}

	private void mockItems(String title, BigDecimal price) {
		when(catalogService.getItems()).thenReturn(Collections.singletonList(new Item("test", title, price)));
	}

}
