package zeus.zeushop.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import zeus.zeushop.model.ListingSell;
import zeus.zeushop.service.ListingSellService;

@ExtendWith(MockitoExtension.class)
class ListingSellControllerTest {

    @InjectMocks
    private ListingSellController listingSellController;

    @Mock
    private ListingSellService listingSellService;

    @Mock
    private Model model;

    private ListingSell listingSell;

    @BeforeEach
    void setUp() {
        String id = UUID.randomUUID().toString();
        listingSell = new ListingSell();
        listingSell.setProduct_id(1);
        listingSell.setProduct_name("Mini Skirt");
        listingSell.setProduct_description("Lorem ipsum dolor sit amet,");
        listingSell.setProduct_quantity(10);
        listingSell.setProduct_price(129000f);
    }

    @Test
    void testListingSellPage() {
        List<ListingSell> listingSells = new ArrayList<>();
        listingSells.add(listingSell);

        when(listingSellService.findAll()).thenReturn(listingSells);

        String viewName = listingSellController.listingSellPage(model);

        assertEquals("list", viewName);
        verify(model).addAttribute("lists", listingSells);
    }
}